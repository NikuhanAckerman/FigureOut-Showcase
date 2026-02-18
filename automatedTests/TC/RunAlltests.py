import subprocess
import os

# Função para rodar um script Python
def run_script(script_name):
    try:
        print(f"Executando {script_name}...")
        result = subprocess.run(['python', script_name], check=True, text=True, capture_output=True)
        print(f"Saída do {script_name}:\n{result.stdout}")
    except subprocess.CalledProcessError as e:
        print(f"Erro ao executar {script_name}:\n{e.stderr}")

# Lista dos scripts a serem executados
scripts = [
    'ChromeTC01.py',
    'ChromeTC02.py',
    'ChromeTC03.py',
    'ChromeTC04.py',
    'ChromeTC05.py',
    'ChromeTC06.py',
    'ChromeTC07.py',
    'ChromeTC08.py',
    'ChromeTC09.py',
    'ChromeTC10.py',
    'ChromeTC11.py',
    'ChromeTC12.py',
    'ChromeTC13.py',
    'ChromeTC14.py',
    'ChromeTC15.py',
    'ChromeTC16.py',
    'ChromeTC17.py',
    'ChromeTC18.py',
    'ChromeTC19.py',
    'ChromeTC20.py',
    'ChromeTC21.py',
    'ChromeTC22.py',
    'ChromeTC23.py',
    'ChromeTC24.py',
    'ChromeTC25.py',
    'ChromeTC26.py',
    'ChromeTC27.py',
    'ChromeTC30.py',
    'ChromeTC32.py'
]

# Executa os scripts na ordem
for script in scripts:
    run_script(script)
    yes = input("Deseja continuar para o próximo teste [y/N]? ").strip().lower()
    if yes != 'y':
        break

    
