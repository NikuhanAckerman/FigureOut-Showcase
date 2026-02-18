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

    def test_filter_products(self):
        
        # Informações gerais.
        select_option_by_value(self, "clientNavigate", "1")
        time.sleep(2)
        
        click_button(self, "seeShop")
        time.sleep(1)

        # Adicionando o produto "Paimon" no carrinho.
        check_checkbox(self, "category-4")
        time.sleep(1)

        click_button(self, "filterButton")
        
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
