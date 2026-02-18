import subprocess
import os

# Função para rodar um script Python
def run_script(script_name):
    try:
        print(f"Executando {script_name}...")
        # Executa o script via subprocess
        result = subprocess.run(['python', script_name], check=True, text=True, capture_output=True)
        print(f"Saída do {script_name}:\n{result.stdout}")
    except subprocess.CalledProcessError as e:
        print(f"Erro ao executar {script_name}:\n{e.stderr}")

# Lista dos scripts a serem executados
scripts = [
    'ChromeCreateTonsOfClients.py',
    'ChromeCreate10CreditCards.py',
    'ChromeCreateProducts.py'
]

# Executa os scripts na ordem
for script in scripts:
    run_script(script)
