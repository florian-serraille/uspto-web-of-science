#!/usr/bin/env bash

# Set variables
USPTO="${USPTO}"
BACKUP_FILE="uspto-data-dump-$(date +%Y-%m-%d).sql"

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
    echo -e "${RED}Variavel de ambiente USPTO não encontrada${BASIC}" >&2
    exit 1
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
  sleep 3
}

export_data() {
  docker exec -it db sh -c "mysqldump -uUSPTO-APP -pUCB USPTO > /var/lib/mysql/${BACKUP_FILE}"

  if [[ "${?}" -eq 0 ]]; then
  echo -e "${GREEN}Backup criado em '${USPTO}/db/${BACKUP_FILE}'${BASIC}"
  else
    echo -e "${RED}Erro ao realizar o backup ${BASIC}" >&2
    stop_container
    exit 1
  fi

  stop_container
}

stop_container() {
  echo "Baixando container 'db'"
  docker stop db >/dev/null
}

do_backup() {

  check_db
  export_data
}

print_header
import_var
is_valid_directory "${USPTO}"
do_backup
