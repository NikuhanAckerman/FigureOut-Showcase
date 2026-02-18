import sys
import os

sys.path.insert(0, os.path.dirname(os.getcwd()))

import unittest
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import time
from ChromeSeleniumFunctions import *

class TransactionTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome.
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver.
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/index")

    def test_make_purchase(self):
        
        # Informações gerais.
        select_option_by_value(self, "clientNavigate", "1")
        time.sleep(2)
        
        click_button(self, "seeShop")
        time.sleep(1)

        # Adicionando o produto "Paimon" no carrinho.
        select_product(self, "Paimon")
        time.sleep(2)

        input_string(self, "quantity", "1")
        time.sleep(1)
        
        click_button(self, "addToCart")
        time.sleep(2)

        click_button(self, "backToShop")
        time.sleep(2)

        # Adicionando o produto "Hu Tao" no carrinho.
        select_product(self, "Hu Tao")
        time.sleep(2)

        input_string(self, "quantity", "1")
        time.sleep(1)
        
        click_button(self, "addToCart")
        time.sleep(2)

        click_button(self, "backToShop")
        time.sleep(2)

        # Entrando no offcanvas do carrinho.
        click_button(self, "cart")
        time.sleep(2)

        click_button(self, "proceedToCheckout")
        time.sleep(2)

        # Página de carrinho.
        # Aplicando cupom promocional.
        input_string(self, "cupom-input", "FIGUREOUT10")
        time.sleep(1)

        click_button(self, "applyCupom")
        time.sleep(1)

        # Selecionando cartões de crédito e endereço.
        select_option_by_value(self, "salesCardsIds", "1")
        time.sleep(1)
        select_option_by_value(self, "salesCardsIds", "11")
        time.sleep(1)

        select_option(self, "address-dropdown", "Minha casa")
        time.sleep(1)

        click_button(self, "btn-finalizar")
        time.sleep(1)

        # Página de finalizar compra.
        input_string(self, "amountPaid1", "300")
        time.sleep(1)
        input_string(self, "amountPaid11", "12.84")
        time.sleep(1)

        click_button(self, "btn-finalizar")
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
