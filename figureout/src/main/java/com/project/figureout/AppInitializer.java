package com.project.figureout;

import com.project.figureout.model.*;
import com.project.figureout.repository.*;
import com.project.figureout.service.CartService;
import com.project.figureout.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class AppInitializer implements CommandLineRunner {

    // enable DELETE and PUT requests:
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private CreditCardBrandRepository creditCardBrandRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PricingGroupRepository pricingGroupRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PromotionalCouponRepository promotionalCouponRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (genderRepository.count() == 0) {
            System.out.println("Populando tabela de gêneros...");

            ArrayList<Gender> genderList = new ArrayList<>();

            Collections.addAll(genderList,
                    new Gender("Masculino"),
                    new Gender("Feminino"),
                    new Gender("Outro")
            );

            genderRepository.saveAll(genderList);

            System.out.println("Tabela de gêneros populada.");
        }

        if(creditCardBrandRepository.count() == 0) {
            System.out.println("Populando tabela de bandeira de cartão de crédito...");

            ArrayList<CreditCardBrand> creditCardBrandList = new ArrayList<>();

            Collections.addAll(creditCardBrandList,
                    new CreditCardBrand("Visa"),
                    new CreditCardBrand("MasterCard"),
                    new CreditCardBrand("American Express"),
                    new CreditCardBrand("Elo")
            );

            creditCardBrandRepository.saveAll(creditCardBrandList);

            System.out.println("Tabela de bandeira de cartão de crédito populada.");
        }

        if(stateRepository.count() == 0) {
            System.out.println("Populando tabela de estados...");

            ArrayList<State> stateList = new ArrayList<>();

            Collections.addAll(stateList,
                    new State("Acre", BigDecimal.valueOf(0.02)),
                    new State("Alagoas", BigDecimal.valueOf(0.02)),
                    new State("Amazonas", BigDecimal.valueOf(0.02)),
                    new State("Amapá", BigDecimal.valueOf(0.02)),
                    new State("Bahia", BigDecimal.valueOf(0.02)),
                    new State("Ceará", BigDecimal.valueOf(0.02)),
                    new State("Distrito Federal", BigDecimal.valueOf(0.02)),
                    new State("Espírito Santo", BigDecimal.valueOf(0.02)),
                    new State("Goiás", BigDecimal.valueOf(0.02)),
                    new State("Maranhão", BigDecimal.valueOf(0.02)),
                    new State("Minas Gerais", BigDecimal.valueOf(0.02)),
                    new State("Mato Grosso do Sul", BigDecimal.valueOf(0.02)),
                    new State("Mato Grosso", BigDecimal.valueOf(0.02)),
                    new State("Pará", BigDecimal.valueOf(0.02)),
                    new State("Paraíba", BigDecimal.valueOf(0.02)),
                    new State("Pernambuco", BigDecimal.valueOf(0.02)),
                    new State("Piauí", BigDecimal.valueOf(0.02)),
                    new State("Paraná", BigDecimal.valueOf(0.02)),
                    new State("Rio de Janeiro", BigDecimal.valueOf(0.02)),
                    new State("Rio Grande do Norte", BigDecimal.valueOf(0.02)),
                    new State("Rondônia", BigDecimal.valueOf(0.02)),
                    new State("Roraima", BigDecimal.valueOf(0.02)),
                    new State("Rio Grande do Sul", BigDecimal.valueOf(0.02)),
                    new State("Santa Catarina", BigDecimal.valueOf(0.02)),
                    new State("Sergipe", BigDecimal.valueOf(0.02)),
                    new State("São Paulo", BigDecimal.valueOf(0.01)),
                    new State("Tocantins", BigDecimal.valueOf(0.02))
            );

            stateRepository.saveAll(stateList);

            System.out.println("Tabela de estados populada.");
        }

        if(countryRepository.count() == 0) {
            System.out.println("Populando tabela de países...");

            ArrayList<Country> countryList = new ArrayList<>();

            Collections.addAll(countryList,
                    new Country("Brasil")
            );

            countryRepository.saveAll(countryList);

            System.out.println("Tabela de países populada.");
        }

        if(pricingGroupRepository.count() == 0) {
            System.out.println("Populando tabela de grupos de precificação...");

            ArrayList<PricingGroup> pricingGroupList = new ArrayList<>();

            Collections.addAll(pricingGroupList,
                    new PricingGroup("Ouro", BigDecimal.valueOf(0.20)),
                    new PricingGroup("Prata", BigDecimal.valueOf(0.15)),
                    new PricingGroup("Bronze", BigDecimal.valueOf(0.10)),
                    new PricingGroup("Ferro", BigDecimal.valueOf(0.05))
            );

            pricingGroupRepository.saveAll(pricingGroupList);

            System.out.println("Tabela de grupos de precificação populada.");
        }

        if(categoryRepository.count() == 0) {
            System.out.println("Populando tabela de categorias...");

            ArrayList<Category> categoryList = new ArrayList<>();

            Collections.addAll(categoryList,
                    new Category("Chaveiro"),
                    new Category("Pré-pintado"),
                    new Category("Model Kit"),
                    new Category("Nendoroid"),
                    new Category("Pelúcia"),
                    new Category("Amiibo"),
                    new Category("Figura de ação")
            );

            categoryRepository.saveAll(categoryList);

            /*
                                new Category("1/4"),
                    new Category("1/6"),
                    new Category("1/7"),
                    new Category("1/8"),
                    new Category("1/16")

             */

            System.out.println("Tabela de categorias populada.");
        }

        if(supplierRepository.count() == 0) {
            System.out.println("Populando tabela de fornecedores...");

            ArrayList<Supplier> supplierList = new ArrayList<>();

            Collections.addAll(supplierList,
                    new Supplier("Fornecedor A"),
                    new Supplier("Fornecedor B"),
                    new Supplier("Fornecedor C"),
                    new Supplier("Fornecedor D")
            );

            supplierRepository.saveAll(supplierList);

            System.out.println("Tabela de fornecedores populada.");
        }

        if(promotionalCouponRepository.count() == 0) {
            System.out.println("Populando tabela de cupons promocionais...");

            ArrayList<PromotionalCoupon> promotionalCouponList = new ArrayList<>();

            LocalDate standardExpiryDate = LocalDate.of(2024, 12, 31);

            Collections.addAll(promotionalCouponList,
                    new PromotionalCoupon("FIGUREOUT90", BigDecimal.valueOf(0.90), standardExpiryDate),
                    new PromotionalCoupon("FIGUREOUT10", BigDecimal.valueOf(0.10), standardExpiryDate)
            );

            promotionalCouponRepository.saveAll(promotionalCouponList);

            System.out.println("Tabela de cupons promocionais populada.");
        }

        if(manufacturerRepository.count() == 0) {
            System.out.println("Populando tabela de fabricantes...");

            ArrayList<Manufacturer> manufacturerList = new ArrayList<>();

            Collections.addAll(manufacturerList,
                    new Manufacturer("Bandai"),
                    new Manufacturer("Medicom"),
                    new Manufacturer("TakaraTomy"),
                    new Manufacturer("Kaiyodo"),
                    new Manufacturer("Good Smile"),
                    new Manufacturer("Nintendo")
            );

            manufacturerRepository.saveAll(manufacturerList);

            System.out.println("Tabela de fabricantes populada.");
        }

        if(sizeRepository.count() == 0) {
            System.out.println("Populando tabela de tamanhos de produto...");

            ArrayList<Size> sizeList = new ArrayList<>();

            Collections.addAll(sizeList,
                    new Size("1/6"),
                    new Size("1/7"),
                    new Size("1/8"),
                    new Size("1/16")
            );

            sizeRepository.saveAll(sizeList);

            System.out.println("Tabela de tamanhos de produto populada.");
        }

        System.out.println("A aplicação foi inicializada com sucesso!");

    }

}
