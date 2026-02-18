import sys
import os

sys.path.insert(0, os.path.dirname(os.getcwd()))

import unittest
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import time
import csv
from ChromeSeleniumFunctions import *

class ProductFormTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/stock/seeStock")

    def test_update_stock(self):

        time.sleep(1)
        click_button(self, "updateStock-1")
        time.sleep(1)

        blank_field(self, "productQuantityAvailable")
        input_string(self, "productQuantityAvailable", "100")
        time.sleep(1)

        blank_field(self, "entryInStockDate")
        input_string(self, "entryInStockDate", "05-02-2024")
        time.sleep(1)
        
        select_option(self, "supplier", "Fornecedor D")
        time.sleep(1)
        
        click_button(self, "updateButton")

        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
