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

    def test_update_product(self):
        
        click_button(self, "updateProduct-6")
        time.sleep(1)

        blank_field(self, "name")
        input_string(self, "name", "Amiibo - Sonic")
        time.sleep(1)

        blank_field(self, "description")
        input_string(self, "description", "Amiibo do personagem Sonic da SEGA.")
        time.sleep(1)

        blank_field(self, "height")
        input_string(self, "height", "7.48")
        time.sleep(1)

        blank_field(self, "width")
        input_string(self, "width", "5.12")
        time.sleep(1)

        blank_field(self, "length")
        input_string(self, "length", "3.58")
        time.sleep(1)

        blank_field(self, "weight")
        input_string(self, "weight", "59")
        time.sleep(1)

        blank_field(self, "purchaseAmount")
        input_string(self, "purchaseAmount", "160.32")
        time.sleep(1)
        
        select_option(self, "categorySelect", "Amiibo")
        time.sleep(1)
        
        select_option(self, "pricingGroupSelect", "Bronze (10.00%)")
        time.sleep(1)

        blank_field(self, "price")
        input_string(self, "price", "285.60")
        time.sleep(1)

        #blank_field(self, "productQuantityAvailable")
        #input_string(self, "productQuantityAvailable", "26")
        #time.sleep(1)

        #blank_field(self, "entryInStockDate")
        ##input_string(self, "entryInStockDate", "07-10-2023")
        #time.sleep(1)
        
        #select_option(self, "supplier", "Fornecedor A")
        #time.sleep(1)
        
        select_option(self, "manufacturer", "Nintendo")
        time.sleep(1)
        
        select_option(self, "size", "1/6")
        time.sleep(1)

        send_image(self, "fileInput", "Images", "sonic.jpg")
        time.sleep(1)
        
        click_button(self, "updateProduct")

        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
