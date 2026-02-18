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
        self.driver.get("http://localhost:8080/products/seeProducts")

    def test_create_product(self):
        
        click_button(self, "createProduct")
        time.sleep(1)
        
        input_string(self, "name", "Amiibo - Mario")
        time.sleep(1)

        input_string(self, "description", "Amiibo do personagem Mario da Nintendo.")
        time.sleep(1)
        
        input_string(self, "height", "19")
        time.sleep(1)
        
        input_string(self, "width", "13")
        time.sleep(1)
        
        input_string(self, "length", "9.1")
        time.sleep(1)
        
        input_string(self, "weight", "140")
        time.sleep(1)
        
        input_string(self, "purchaseAmount", "185.56")
        time.sleep(1)
        
        select_option(self, "categorySelect", "Amiibo")
        time.sleep(1)
        
        select_option(self, "pricingGroupSelect", "Bronze (10.00%)")
        time.sleep(1)

        input_string(self, "price", "299.90")
        time.sleep(1)

        input_string(self, "productQuantityAvailable", "23")
        time.sleep(1)

        input_string(self, "entryInStockDate", "06-10-2023")
        time.sleep(1)
        
        select_option(self, "supplier", "Fornecedor B")
        time.sleep(1)
        
        select_option(self, "manufacturer", "Nintendo")
        time.sleep(1)
        
        select_option(self, "size", "1/6")
        time.sleep(1)

        send_image(self, "fileInput", "Images", "mario.jpg")
        time.sleep(1)
        
        click_button(self, "createButton")

        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
