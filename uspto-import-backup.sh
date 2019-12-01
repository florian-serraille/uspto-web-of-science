#!/usr/bin/env bash

# Set variables
USPTO="${USPTO}"
BACKUP_FILE="${1}"

# Colors
RED='\e[1;31m'
GREEN='\e[1;32m'
BLUE='\e[1;34m'
BASIC='\e[0;m'

print_header() {
  echo -e "${BLUE}"
  echo '
__/\\\________/\\\________/\\\\\\\\\__/\\\\\\\\\\\\\___
 _\/\\\_______\/\\\_____/\\\////////__\/\\\/////////\\\_
  _\/\\\_______\/\\\___/\\\/___________\/\\\_______\/\\\_
   _\/\\\_______\/\\\__/\\\_____________\/\\\\\\\\\\\\\\__
    _\/\\\_______\/\\\_\/\\\_____________\/\\\/////////\\\_
     _\/\\\_______\/\\\_\//\\\____________\/\\\_______\/\\\_
      _\//\\\______/\\\___\///\\\__________\/\\\_______\/\\\_
       __\///\\\\\\\\\/______\////\\\\\\\\\_\/\\\\\\\\\\\\\/__
        ____\/////////___________\/////////__\/////////////____
'
  echo -e ${BASIC}
}

import_var() {
  if [[ -e ~/.bash_profile ]]; then
    source ~/.bash_profile
  else
    echo -e "${RED}Variavel de ambiente USPTO não encontrada${BASIC}"
  fi
}

is_valid_directory() {

  if [[ ! -d "${USPTO}" ]]; then
    echo -e "${RED}'${USPTO}' não é um diretório válido ${BASIC}" >&2
    exit 1
  elif [[ ! -e ${USPTO}/${DOCKER_COMPOSE} ]]; then
    echo -e "${RED}Arquivo ${DOCKER_COMPOSE} não encontrado em '${USPTO}/${DOCKER_COMPOSE}'${BASIC}" >&2
    exit 1
  fi
}

check_db() {

  if [[ $(docker ps -a -f name=db | wc -l) -eq 1 ]]; then
    echo -e "${RED}Container 'db' não exista${BASIC}" >&2
    exit 1
  fi

  if [[ $(docker ps -f name=db | wc -l) -eq 1 ]]; then
    start_db
  fi

}

start_db() {
  echo "Levantando container 'db'"
  docker start db >/dev/null
  sleep 4
}

is_valid_backup_file() {

  if [[ -z ${BACKUP_FILE} ]]; then
    echo -e "${RED}Deve ser informado o caminho do arquivo de backup${BASIC}" >&2
    exit 1
  fi

  if [[ ! -e ${BACKUP_FILE} ]]; then
    echo -e "${RED}Arquivo de backup ${BACKUP_FILE} não encontrado${BASIC}" >&2
    exit 1
  fi

  if ! [[ "${BACKUP_FILE}" =~ uspto-data-dump-[0-9]{4}-[0-9]{2}-[0-9]{2}.sql$ ]]; then
    echo -e "${RED}Arquivo de backup '${BACKUP_FILE}' não bate com o padrão 'uspto-data-dump-YYYY-MM-DD.sql'${BASIC}" >&2
    exit 1
  fi

  dir="$(dirname ${BACKUP_FILE}})"
  if ! [[ "${dir}" -ef "${USPTO}/db" ]]; then

    echo "Copiando arquivo de backup para o volume compartilhado com o container 'db'"
    if [ -w /tmp/myfile.txt ]; then
      cp -v "${BACKUP_FILE}" "${USPTO}/db/"
    else
      echo "É preciso permissão para mover arquivo."
      sudo cp -v "${BACKUP_FILE}" "${USPTO}/db/"
    fi
  fi

  if ! [[ "${?}" -eq 0 ]]; then
    echo -e "${RED}Erro ao movimentar arquivo${BASIC}" >&2
    exit 1
  fi

}

import_data() {

  echo "Importando ${BACKUP_FILE}"
  docker exec -it db sh -c "mysql -uUSPTO-APP -pUCB USPTO < /var/lib/mysql/$(basename ${BACKUP_FILE})"
  if [[ "${?}" -eq 0 ]]; then
    echo -e "${GREEN}Backup importado com sucesso${BASIC}"
    stop_container
  else
    echo -e "${RED}Erro ao realizar a importação do backup${BASIC}" >&2
    stop_container
    exit 1
  fi

}

stop_container() {
  echo "Baixando container 'db'"
  docker stop db >/dev/null
}

get_backup() {

  check_db
  import_data
}

print_header
import_var
is_valid_directory "${USPTO}"
is_valid_backup_file
get_backup
