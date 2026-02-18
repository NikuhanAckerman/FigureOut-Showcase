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

    def test_exchange_product(self):
        
        select_option_by_value(self, "clientNavigate", "1")
        time.sleep(2)
        
        click_button(self, "seeShop")
        time.sleep(1)

        click_button(self, "clientProfile")
        time.sleep(1)

        click_button(self, "purchases")
        time.sleep(1)

        click_button(self, "moreInformation-21")
        time.sleep(1)

        click_button(self, "createExchange-21")
        time.sleep(2)

        input_string(self, "exchangeInput-1", "1")
        time.sleep(1)

        input_string(self, "exchangeInput-2", "1")
        time.sleep(1)

        click_button(self, "makeExchange")
        time.sleep(1)

        accept_confirm_box(self)
        time.sleep(6)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
