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
        # Configura as opções do Chrome.
        chrome_options = Options()
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        # Iniciliza o WebDriver.
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        self.driver.get("http://localhost:8080/showAllClients")

    def test_update_client(self):

        click_button(self, "updateClient-11")

        blank_field(self, "name")
        input_string(self, "name", "Nicolas Satil")
        time.sleep(1)

        blank_field(self, "email")
        input_string(self, "email", "nicoSatil@gmail.com")
        time.sleep(1)

        blank_field(self, "password")
        input_string(self, "password", "senhaMuitoForte123@")
        time.sleep(1)

        blank_field(self, "confirmPassword")
        input_string(self, "confirmPassword", "senhaMuitoForte123@")
        time.sleep(1)

        blank_field(self, "birthday")
        input_string(self, "birthday", "18-03-2004")
        time.sleep(1)

        blank_field(self, "cpf")
        input_string(self, "cpf", "528.196.489-56")
        time.sleep(1)

        check_checkbox(self, "enabled")
        time.sleep(1)

        select_radio(self, "gender-1", "1")
        time.sleep(1)

        # Informações de Telefone.
        select_radio(self, "phoneTrue", "true")
        time.sleep(1)

        blank_field(self, "ddd")
        input_string(self, "ddd", "11")
        time.sleep(1)

        blank_field(self, "phoneNumber")
        input_string(self, "phoneNumber", "78941234")
        time.sleep(1)

        
        click_button(self, "updateClient")
        # Espera alguns segundos para antes de fechar o browser.
        time.sleep(3)

    def tearDown(self):
        # Fecha o browser
        self.driver.quit()

if __name__ == "__main__":
    unittest.main()
