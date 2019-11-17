#!bin/bash

set_uspto_dir(){

	if [[ -n ${HOME} ]]; then
			return "${HOME}/uspto"
	else 
			return "/tmp/uspto"
	fi

}


create_backup_file(){


	if [[ $(docker ps -f "name=db" | wc -l) -eq 1 ]]; then
		
		echo "No DB container found, exit..."
		exit 1
	fi

	if [[ $(docker ps -f "name=db" | wc -l) -eq 2 ]]; then
		echo "Starting DB container"
		docker start db > /dev/null
		sleep 3
	fi

	
	docker exec -it db sh -c "mysqldump -uUSPTO-APP -pUCB USPTO > /var/lib/mysql/${backup_file}" 2> /dev/null

}

extract_backup_file(){

	mkdir -p ${uspto_dir}
	mv "${volume}/${backup_file}" "${uspto_dir}/${backup_file}"

}

main(){
	
#volume=$(docker inspect db | grep -e "Source.*volumes" | cut -d '"' -f 4)
#backup_file=uspto-data-dump.sql
#uspto_dir=${set_uspto_dir}
	
#create_backup_file
#extract_backup_file

#echo "Backup file available in ${uspto_dir}/${backup_file}"

}

main
