package com.project.figureout.controller;

import com.project.figureout.ClientNavigator;
import com.project.figureout.dto.UpdateStockDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.ProductsActivationRepository;
import com.project.figureout.repository.StockHistoryRepository;
import com.project.figureout.repository.SupplierRepository;
import com.project.figureout.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Autowired
    private ProductsActivationRepository productsActivationRepository;

    private ClientNavigator clientNavigator;

    // Define o método para lidar com requisições GET para o caminho "/stock"
    @GetMapping("/seeStock")
    public String showStockGet(Model model) {

        List<Stock> stocks = stockService.getAllProductsInStock();

        model.addAttribute("stocks", stocks);

        return "stock";
    }

    // Define o método para lidar com requisições GET para o caminho "/createStock"
    @GetMapping("/updateStock/{productId}")
    public String updateStock(Model model, @PathVariable long productId) {
        Stock stock = stockService.getProductInStockByProductId(productId);
        UpdateStockDTO updateStockDTO = new UpdateStockDTO();

        updateStockDTO.setSupplier(stock.getSupplier()
                .stream()
                .map(Supplier::getId)
                .collect(Collectors.toList()));

        if(stock.getLatestDropDate() != null ) {

            if(stock.getLatestEntryDate().isAfter(stock.getLatestDropDate())) {
                updateStockDTO.setDateChangeOfStockQuantity(stock.getLatestEntryDate());
            } else {
                updateStockDTO.setDateChangeOfStockQuantity(stock.getLatestDropDate());
            }

        } else {
            updateStockDTO.setDateChangeOfStockQuantity(stock.getLatestEntryDate());
        }

        updateStockDTO.setProductQuantityAvailable(stock.getProductQuantityAvailable());

        List<Supplier> supplierList = supplierRepository.findAll();

        model.addAttribute("productId", productId);
        model.addAttribute("supplierList", supplierList);
        model.addAttribute("updateStockDTO", updateStockDTO);

        return "updateStock";
    }

    @PutMapping("/updateStock/{productId}")
    public String updateStock(Model model, @PathVariable long productId, @ModelAttribute UpdateStockDTO updateStockDTO) {
        Stock stock = stockService.getProductInStockByProductId(productId);

        StockHistory stockHistory = new StockHistory();
        stockHistory.setDateChangeOfStockQuantity(updateStockDTO.getDateChangeOfStockQuantity());
        stockHistory.setSupplier(stockService.getSupplierByListOfIds(updateStockDTO.getSupplier()));
        stockHistory.setProductQuantityAvailable(updateStockDTO.getProductQuantityAvailable());
        stockHistory.setProductQuantityAvailablePreviously(stock.getProductQuantityAvailable());
        stockHistory.setStock(stock);
        stockHistoryRepository.save(stockHistory);

        stock.setSupplier(stockService.getSupplierByListOfIds(updateStockDTO.getSupplier()));

        if(updateStockDTO.getProductQuantityAvailable() >= stock.getProductQuantityAvailable()) {
            stock.setLatestEntryDate(updateStockDTO.getDateChangeOfStockQuantity());
        } else {
            stock.setLatestDropDate(updateStockDTO.getDateChangeOfStockQuantity());
        }

        stock.setProductQuantityAvailable(updateStockDTO.getProductQuantityAvailable());

        stockService.saveProductInStock(stock);

        if(stock.getProductQuantityAvailable() <= 0) {
            Product product = stock.getProduct();

            if(product.isActive()) {
                ProductsActivation productsActivation = new ProductsActivation();
                productsActivation.setActive(false);
                productsActivation.setReason("Produto inativado por estar fora de estoque.");
                productsActivation.setDateTime(LocalDateTime.now());
                productsActivation.setProduct(product);
                productsActivation.setCategory(ProductActivationEnum.FORA_DE_MERCADO);
                productsActivationRepository.save(productsActivation);

                product.getProductActivations().add(productsActivation);
                product.setActive(productsActivation.isActive());

                productService.saveProduct(product);
            }

        }

        return "redirect:/stock/seeStock";
    }

    @GetMapping("/getStockDateOfLastEntry/{stockId}")
    @ResponseBody
    public LocalDate getStockDateOfLastEntry(@PathVariable long stockId) {
        Stock stock = stockService.getProductInStockByProductId(stockId);
        return stock.getLatestEntryDate();
    }

    @GetMapping("/getStockDateOfLastDrop/{stockId}")
    @ResponseBody
    public LocalDate getStockDateOfLastDrop(@PathVariable long stockId) {
        Stock stock = stockService.getProductInStockByProductId(stockId);
        return stock.getLatestDropDate();
    }

    @GetMapping("/getStockHistoryInfo/{stockId}")
    @ResponseBody
    public List<StockHistory> getStockHistoryInfo(@PathVariable long stockId) {
        return stockService.getStockHistoryByStockId(stockId);
    }

}
