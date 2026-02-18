package com.project.figureout.service;

import com.project.figureout.dto.ProductDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Autowired
    private ProductsActivationRepository productsActivationRepository;

    public List<Stock> getAllProductsInStock() {
        return stockRepository.findAll();
    }

    public Stock getProductInStockByProductId(long id) {
        return stockRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado em estoque."));
    }

    public List<Supplier> getSupplierByListOfIds(List<Long> idList) {
        return supplierRepository.findAllById(idList);
    }

    public List<StockHistory> getStockHistoryByStockId(long id) {

        List<StockHistory> stockHistoryList = new ArrayList<>();

        for(StockHistory stockHistory: stockHistoryRepository.findAll()) {

            if(stockHistory.getStock().getId() == id) {
                stockHistoryList.add(stockHistory);
            }

        }

        return stockHistoryList;

    }

    public void saveProductInStock(Stock stock) {
        stockRepository.save(stock);
    }

    public void productInStockDataSetter(Stock stock, Product product, ProductDTO productDTO) {

        stock.setSupplier(getSupplierByListOfIds(productDTO.getStockDTO().getSupplier()));
        stock.setProductQuantityAvailable(productDTO.getStockDTO().getProductQuantityAvailable());
        stock.setProductPurchaseAmount(product.getPurchaseAmount());

        stock.setInitialEntryDate(productDTO.getStockDTO().getEntryInStockDate());
        stock.setLatestEntryDate(productDTO.getStockDTO().getLatestEntryDate());

        stock.setProduct(product);

    }

    public void dropInStock(Stock stock, int quantity) {

        StockHistory stockHistory = new StockHistory();
        stockHistory.setDateChangeOfStockQuantity(LocalDate.now());
        stockHistory.setSupplier(getSupplierByListOfIds(stock.getSupplier().stream().map(Supplier::getId).collect(Collectors.toList())));
        stockHistory.setProductQuantityAvailablePreviously(stock.getProductQuantityAvailable());
        stockHistory.setStock(stock);

        stock.setProductQuantityAvailable(stock.getProductQuantityAvailable() - quantity);
        stock.setLatestDropDate(LocalDate.now());

        stockHistory.setProductQuantityAvailable(stock.getProductQuantityAvailable());
        stockHistoryRepository.save(stockHistory);

        saveProductInStock(stock);

        if(stock.getProductQuantityAvailable() <= 0) {
            Product product = stock.getProduct();

            if(product.isActive()) {
                ProductsActivation productsActivation = new ProductsActivation();
                productsActivation.setActive(false);
                productsActivation.setReason("Produto inativado por estar fora de estoque.");
                productsActivation.setDateTime(LocalDate.now().atStartOfDay());
                productsActivation.setProduct(product);
                productsActivation.setCategory(ProductActivationEnum.FORA_DE_MERCADO);
                productsActivationRepository.save(productsActivation);

                product.getProductActivations().add(productsActivation);
                product.setActive(productsActivation.isActive());

                productRepository.save(product);
            }

        }

    }

    public void dropInStockList(HashMap<Stock, Integer> stockProductsQuantityDrop) {

        for (Map.Entry<Stock, Integer> entry : stockProductsQuantityDrop.entrySet()) {
            Stock key = entry.getKey();
            Integer value = entry.getValue();

            dropInStock(key, value);

        }

    }

    public void addInStock(Stock stock, int quantity) {

        StockHistory stockHistory = new StockHistory();
        stockHistory.setDateChangeOfStockQuantity(LocalDate.now());
        stockHistory.setSupplier(getSupplierByListOfIds(stock.getSupplier().stream().map(Supplier::getId).collect(Collectors.toList())));
        stockHistory.setStock(stock);
        stockHistory.setProductQuantityAvailablePreviously(stock.getProductQuantityAvailable());

        stock.setProductQuantityAvailable(stock.getProductQuantityAvailable() + quantity);
        stock.setLatestEntryDate(LocalDate.now());

        stockHistory.setProductQuantityAvailable(stock.getProductQuantityAvailable());
        stockHistoryRepository.save(stockHistory);

        saveProductInStock(stock);
    }

    public void addInStockList(HashMap<Stock, Integer> stockProductsQuantityAdd) {

        for (Map.Entry<Stock, Integer> entry : stockProductsQuantityAdd.entrySet()) {
            Stock key = entry.getKey();
            Integer value = entry.getValue();

            addInStock(key, value);
        }

    }


}
