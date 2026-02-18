package com.project.figureout.controller;

import com.project.figureout.ClientNavigator;
import com.project.figureout.dto.*;
import com.project.figureout.model.*;
import com.project.figureout.repository.SupplierRepository;
import com.project.figureout.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ClientService clientService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private NotificationService notificationService;

    private ClientNavigator clientNavigator;

    @GetMapping("/seeProducts")
    public String showProductsGet(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "adminSeeProducts";
    }

    @GetMapping("/seeSales")
    public String showSalesGet(Model model) {
        List<Product> products =  productService.getAllProducts();
        model.addAttribute("products", products);

        return "adminSalesView";
    }

    @GetMapping("/createProduct")
    public String createProductGet(Model model) {
        List<Category> categoryList = productService.getAllCategories();
        List<PricingGroup> pricingGroupList = productService.getAllPricingGroups();
        List<Supplier> supplierList = supplierRepository.findAll();
        List<Manufacturer> manufacturerList = manufacturerService.getAllManufacturers();
        List<Size> sizeList = sizeService.getAllSizes();
        ProductDTO productDTO = new ProductDTO();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pricingGroupList", pricingGroupList);
        model.addAttribute("supplierList", supplierList);
        model.addAttribute("productDTO", productDTO);
        model.addAttribute("manufacturerList", manufacturerList);
        model.addAttribute("sizeList", sizeList);

        return "createProduct";
    }

    @PostMapping("/createProduct")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO, BindingResult result, Model model) throws IOException {

        if(result.hasErrors()) {
            List<Category> categoryList = productService.getAllCategories();
            List<PricingGroup> pricingGroupList = productService.getAllPricingGroups();
            List<Supplier> supplierList = supplierRepository.findAll();
            List<Manufacturer> manufacturerList = manufacturerService.getAllManufacturers();
            List<Size> sizeList = sizeService.getAllSizes();

            model.addAttribute("categoryList", categoryList);
            model.addAttribute("pricingGroupList", pricingGroupList);
            model.addAttribute("supplierList", supplierList);
            model.addAttribute("manufacturerList", manufacturerList);
            model.addAttribute("sizeList", sizeList);

            return "createProduct";
        }

        Product product = new Product();
        Stock stock = new Stock();

        productService.productDataSetter(product, productDTO);
        System.out.println(product.getName());

        productDTO.getStockDTO().setLatestEntryDate(productDTO.getStockDTO().getEntryInStockDate());

        stockService.productInStockDataSetter(stock, product, productDTO);
        stockService.saveProductInStock(stock);

        return "redirect:/products/seeProducts";
    }

    @GetMapping("/productPicture/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);

        byte[] image = product.getPicture();
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProductById(id);

        return "redirect:/products/seeProducts";
    }

    @GetMapping("/shop")
    public String showShop(Model model) {
        List<Product> products = (List<Product>) model.asMap().get("products");

        if(products == null) {
            products =  productService.getAllActiveProducts();
        }

        Client client = clientService.getClientById(clientNavigator.getInstance().getClientId());
        int notificationQuantity = notificationService.getClientNotifications(client.getId()).size();
        List<Category> availableProductCategories = productService.getAllCategories();
        List<Manufacturer> availableProductManufacturers = manufacturerService.getAllManufacturers();
        List<Size> availableProductSizes = sizeService.getAllSizes();

        model.addAttribute("products", products);
        model.addAttribute("availableProductManufacturers", availableProductManufacturers);
        model.addAttribute("availableProductCategories", availableProductCategories);
        model.addAttribute("availableProductSizes", availableProductSizes);
        model.addAttribute("cart", client.getCartList().getLast()); // always get the last card added to the client's cart list
        model.addAttribute("clientId", clientNavigator.getInstance().getClientId());
        model.addAttribute("notificationQuantity", notificationQuantity);
        return "shop";
    }

    @GetMapping("/specificProduct/{id}")
    public String showSpecificProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        Stock stock = stockService.getProductInStockByProductId(id);
        Client client = clientService.getClientById(clientNavigator.getInstance().getClientId());
        int notificationQuantity = notificationService.getClientNotifications(client.getId()).size();
        model.addAttribute("clientId", client.getId());
        model.addAttribute("notificationQuantity", notificationQuantity);
        model.addAttribute("stock", stock);
        model.addAttribute("changeCartProductQuantityDTO", new ChangeCartProductQuantityDTO());
        model.addAttribute("product", product);
        model.addAttribute("cart", client.getCartList().getLast());

        return "product";
    }

    @GetMapping("/updateProduct/{id}")
    public String updateProductGet(@PathVariable Long id, Model model) throws IOException {
        Product product = productService.getProductById(id);
        List<Supplier> supplierList = supplierRepository.findAll();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getId());
        productDTO.setStockDTO(new StockDTO());

        productService.populateProductDTO(productDTO, product);

        List<Category> categoryList = productService.getAllCategories();
        List<PricingGroup> pricingGroupList = productService.getAllPricingGroups();
        List<Manufacturer> manufacturerList = manufacturerService.getAllManufacturers();
        List<Size> sizeList = sizeService.getAllSizes();

        model.addAttribute("productDTO", productDTO);
        model.addAttribute("productId", id);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pricingGroupList", pricingGroupList);
        model.addAttribute("supplierList", supplierList);
        model.addAttribute("manufacturerList", manufacturerList);
        model.addAttribute("sizeList", sizeList);

        return "updateProduct";
    }

    @PutMapping("/updateProduct/{id}")
    public String updateProductPut(@PathVariable Long id, @Valid @ModelAttribute ProductDTO productDTO, BindingResult result, Model model) throws IOException {
        if(result.hasErrors()) {
            List<Supplier> supplierList = supplierRepository.findAll();
            List<Category> categoryList = productService.getAllCategories();
            List<PricingGroup> pricingGroupList = productService.getAllPricingGroups();
            List<Manufacturer> manufacturerList = manufacturerService.getAllManufacturers();
            List<Size> sizeList = sizeService.getAllSizes();

            model.addAttribute("productId", id);
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("pricingGroupList", pricingGroupList);
            model.addAttribute("supplierList", supplierList);
            model.addAttribute("manufacturerList", manufacturerList);
            model.addAttribute("sizeList", sizeList);

            return "updateProduct";
        }

        Product product = productService.getProductById(id);

        productService.updateProduct(product, productDTO);

        return "redirect:/products/seeProducts";
    }

    @GetMapping("/getProductCategories/{id}")
    @ResponseBody
    public List<Category> getProductCategories(@PathVariable Long id) {
        return productService.getProductById(id).getCategories();
    }

    @GetMapping("/getProductGeneralInfo/{id}")
    @ResponseBody
    public Product getProductGeneralInfo(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/getProductActivationInfo/{id}")
    @ResponseBody
    public List<ProductsActivation> getProductActivationInfo(@PathVariable Long id) {
        return productService.getProductById(id).getProductActivations();
    }

    // Filtro de produtos no CRUD de produtos
    @GetMapping("/filterProducts")
    public String getProducts(@RequestParam(required = false) Long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) Float height,
                              @RequestParam(required = false) Float width,
                              @RequestParam(required = false) Float weight,
                              @RequestParam(required = false) Float length,
                              @RequestParam(required = false) BigDecimal purchaseAmount,
                              @RequestParam(required = false) BigDecimal price,
                              @RequestParam(required = false) PricingGroup pricingGroup,
                              Model model) {

        List<Product> products = productService.filterProducts(id, name, height, width, weight, length, purchaseAmount, price, pricingGroup);

        model.addAttribute("products", products);
        model.addAttribute("filterId", id);
        model.addAttribute("filterName", name);
        model.addAttribute("filterHeight", height);
        model.addAttribute("filterWidth", width);
        model.addAttribute("filterWeight", weight);
        model.addAttribute("filterLength", length);
        model.addAttribute("filterPurchaseAmount", purchaseAmount);
        model.addAttribute("filterPrice", price);
        model.addAttribute("filterPricingGroup", pricingGroup);

        return "adminSeeProducts";
    }

    // Filtro de produtos na loja.
    @GetMapping("/shop/filter")
    public String filterShopProducts(
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) BigDecimal price,
            RedirectAttributes redirectAttributes) {

        List<Product> products = productService.filterShop(category, manufacturer, size, price);

        redirectAttributes.addFlashAttribute("products", products);

        return "redirect:/products/shop";
    }

    @GetMapping("/shop/search")
    public String searchShopProducts(@RequestParam(required = false) String name, RedirectAttributes redirectAttributes) {

        List<Product> products = productService.searchShop(name);

        redirectAttributes.addFlashAttribute("products", products);

        return "redirect:/products/shop";
    }

}
