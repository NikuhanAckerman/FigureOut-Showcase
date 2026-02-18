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
        self.driver.get("http://localhost:8080/sales/seeSales")

    def test_approve_exchange(self):
        
        select_option(self, "saleStatus21", "TROCA_AUTORIZADA")
        time.sleep(1)
        
        select_option(self, "saleStatus21", "EM_TROCA")
        time.sleep(1)

        select_option(self, "saleStatus21", "TROCA_RECEBIDA")
        time.sleep(1)
        
        select_option(self, "saleStatus21", "TROCA_FINALIZADA")
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
