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

class ClientTest(unittest.TestCase):
    def setUp(self):
        # Configura as opções do Chrome
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/showAllClients")

    def test_update_balance(self):

        click_button(self, "seeCreditCards-1")
        time.sleep(1)
        
        click_button(self, "creditCardCollapse-12")
        time.sleep(1)

        click_button(self, "updateBalanceCreditCard-12")
        time.sleep(1)

        blank_field(self, "balance")
        input_string(self, "balance", "99999,99")
        time.sleep(1)

        click_button(self, "updateBalanceCreditCard")
        
        # Espera alguns segundos para o resultado.
        time.sleep(4)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
