package com.project.figureout.service;

import com.project.figureout.dto.ProductDTO;
import com.project.figureout.model.*;
import com.project.figureout.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PricingGroupRepository pricingGroupRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductsActivationRepository productsActivationRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllActiveProducts() {
        List<Product> allProducts = getAllProducts();
        List<Product> activeProducts = new ArrayList<>();

        for(Product product : allProducts) {
            if(product.isActive()) {
                activeProducts.add(product);
            }
        }
        return activeProducts;
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado com base no ID."));
    }

    public void deleteProductById(long id) {
        productRepository.deleteById(id); // add exception throwing to this later, apparently this doesnt throw EmptyResultDataAccessException anymore
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<PricingGroup> getAllPricingGroups() {
        return pricingGroupRepository.findAll();
    }

    public void productDataSetter(Product product, ProductDTO productDTO) {
        System.out.println("calling product data setter");
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setHeight(productDTO.getHeight());
        product.setWidth(productDTO.getWidth());
        product.setWeight(productDTO.getWeight());
        product.setLength(productDTO.getWidth());
        product.setPurchaseAmount(productDTO.getPurchaseAmount());

        List<Category> categoryListAddToProduct = new ArrayList<>();
        categoryListAddToProduct.addAll(categoryRepository.findAllById(productDTO.getCategoriesIds()));

        product.setCategories(categoryListAddToProduct);

        product.setPricingGroup(pricingGroupRepository.findById(productDTO.getPricingGroup()).orElseThrow(() -> new NoSuchElementException("Grupo de precificação não encontrado.")));

        product.setPrice(productDTO.getPrice());

        product.setPicture(saveProductPicture(productDTO));

        LocalDateTime now = LocalDateTime.now();

        saveProduct(product);

        if(product.isActive() != productDTO.isActive()) {

            ProductsActivation productsActivation = new ProductsActivation();
            productsActivation.setActive(productDTO.isActive());
            productsActivation.setReason(productDTO.getReasonForInactivationOrActivation());
            productsActivation.setDateTime(now);
            productsActivation.setProduct(product);
            productsActivation.setCategory(ProductActivationEnum.OUTRA_RAZAO);
            productsActivationRepository.save(productsActivation);

            product.getProductActivations().add(productsActivation);

            productRepository.save(product);

        }

        product.setActive(productDTO.isActive());
        saveProduct(product);

        product.setManufacturer(manufacturerRepository.findById(productDTO.getManufacturer()).orElseThrow(() -> new NoSuchElementException("Fabricante não encontrada.")));
        product.setSize(sizeRepository.findById(productDTO.getSize()).orElseThrow(() -> new NoSuchElementException("Tamanho de produto não encontrado.")));

        saveProduct(product);
    }



    public byte[] saveProductPicture(ProductDTO productDTO) {

        try {
            MultipartFile multipartFile = productDTO.getProductImage();
            if (!multipartFile.isEmpty()) {
                byte[] imageData = multipartFile.getBytes();
                return imageData;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Handle the error appropriately
        }

        return null;
    }

    public void populateProductDTO(ProductDTO productDTO, Product product) throws IOException {

        productDTO.setActive(product.isActive());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setHeight(product.getHeight());
        productDTO.setWidth(product.getWidth());
        productDTO.setWeight(product.getWeight());
        productDTO.setLength(product.getLength());
        productDTO.setPurchaseAmount(product.getPurchaseAmount());

        List<Long> productCategoryIdList = product.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        productDTO.setCategoriesIds(productCategoryIdList);
        productDTO.setPricingGroup(product.getPricingGroup().getId());
        productDTO.setPrice(product.getPrice());

        productDTO.setManufacturer(product.getManufacturer().getId());
        productDTO.setSize(product.getSize().getId());

    }

    public void updateProduct(Product product, ProductDTO productDTO) {
        productDataSetter(product, productDTO);

        saveProduct(product);
    }

    public void inactivateProduct(Product product) {
        product.setActive(false);
        saveProduct(product);
    }

    // Método para filtrar os atributos dos produtos.
    public List<Product> filterProducts(Long id,
                                        String name,
                                        Float height,
                                        Float width,
                                        Float weight,
                                        Float length,
                                        BigDecimal purchaseAmount,
                                        BigDecimal price,
                                        PricingGroup pricingGroup) {
        List<Product> products = getAllProducts();

        System.out.println(id);
        System.out.println(name);
        System.out.println(height);
        System.out.println(width);
        System.out.println(weight);
        System.out.println(length);
        System.out.println(purchaseAmount);
        System.out.println(price);

        if (id != null && id > 0) {
            products = products.stream()
                    .filter(product -> product.getId() == id) // Comparação direta
                    .collect(Collectors.toList());
        }
        if (name != null && !name.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (name != null && !name.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (height != null) {
            products = products.stream()
                    .filter(product -> product.getHeight() != null && product.getHeight().equals(height)) // Verificação de igualdade exata
                    .collect(Collectors.toList());
        }
        if (width != null) {
            products = products.stream()
                    .filter(product -> product.getWidth() != null && product.getWidth().equals(width)) // Verificação de igualdade exata
                    .collect(Collectors.toList());
        }
        if (weight != null) {
            products = products.stream()
                    .filter(product -> product.getWeight() != null && product.getWeight().equals(weight)) // Verificação de igualdade exata
                    .collect(Collectors.toList());
        }
        if (length != null) {
            products = products.stream()
                    .filter(product -> product.getLength() != null && product.getLength().equals(length)) // Verificação de igualdade exata
                    .collect(Collectors.toList());
        }
        if (purchaseAmount != null) {
            products = products.stream()
                    .filter(product -> product.getPurchaseAmount() != null && product.getPurchaseAmount().compareTo(purchaseAmount) == 0) // Comparação exata com BigDecimal
                    .collect(Collectors.toList());
        }
        if (price != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() != null && product.getPrice().compareTo(price) == 0) // Comparação exata com BigDecimal
                    .collect(Collectors.toList());
        }

        if (pricingGroup != null) {
            products = products.stream()
                    .filter(product -> product.getPricingGroup() != null && product.getPricingGroup().equals(pricingGroup))
                    .collect(Collectors.toList());
        }

        if(products.isEmpty()) {
            System.out.println("sem filtro");
        } else {
            System.out.println("filtro");
        }

        return products.isEmpty() ? new ArrayList<>() : products; // Retornar uma lista vazia se nenhum filtro bater
    }

    // Método para filtrar os atributos dos produtos na loja.
    public List<Product> filterShop(
                                        List<String> category,
                                        String manufacturer,
                                        String size,
                                        BigDecimal price) {
        List<Product> products = getAllActiveProducts();

        System.out.println(category);
        System.out.println(manufacturer);
        System.out.println(size);
        System.out.println(price);

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getCategories().stream().map(Category::getName).anyMatch(category::contains))
                    .collect(Collectors.toList());
        }

        if (manufacturer != null && !manufacturer.equals("Todos")) {
            System.out.println("manufacturer wanted: " + manufacturer);
            for(Product currentProduct: products) {
                System.out.println("Product " + currentProduct.getName() + " Manufacturer: " + currentProduct.getManufacturer().getName());
            }

            products = products.stream()
                    .filter(product -> product.getManufacturer().getName().equalsIgnoreCase(manufacturer))
                    .collect(Collectors.toList());
        }

        if (size != null && !size.equals("Todos")) {
            System.out.println("size wanted: " + size);
            for(Product currentProduct: products) {
                System.out.println("Product " + currentProduct.getName() + " Size: " + currentProduct.getSize().getName());
            }

            products = products.stream()
                    .filter(product -> product.getSize().getName().equalsIgnoreCase(size))
                    .collect(Collectors.toList());
        }

        if (price != null) {
            System.out.println("price " + price);
            System.out.println(price.compareTo(BigDecimal.valueOf(5000.00)) >= 0);

            if(price.compareTo(BigDecimal.valueOf(5000.00)) >= 0) {
                products = products.stream()
                        .filter(product -> product.getPrice().compareTo(price) >= 0) // Comparação exata com BigDecimal
                        .collect(Collectors.toList());

                return products;
            }

            if(!(price.compareTo(BigDecimal.valueOf(50.00)) <= 0)) {
                products = products.stream()
                        .filter(product -> product.getPrice().compareTo(price) <= 0) // Comparação exata com BigDecimal
                        .collect(Collectors.toList());
            }

        }

        if(products.isEmpty()) {
            System.out.println("sem filtro");
        } else {
            System.out.println("filtro");
        }

        return products.isEmpty() ? new ArrayList<>() : products;
    }

    public List<Product> searchShop(String name) {
        List<Product> products = getAllActiveProducts();

        System.out.println(name);

        if (name != null && !name.isEmpty()) {
            products = products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(products.isEmpty()) {
            System.out.println("sem filtro");
        } else {
            System.out.println("filtro");
        }

        return products.isEmpty() ? new ArrayList<>() : products;
    }


}
