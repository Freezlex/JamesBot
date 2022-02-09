useDefault(){
  MYSQL_PORT=3306;
  ADMINER_PORT=8080;
}

promptForPorts(){
  useDefault;
  read -rp "On which port should MySql listen on ? [$MYSQL_PORT] : " newMysql
  if [ "$newMysql" != '' ]; then
    export MYSQL_PORT=$newMysql;
  else
    export MYSQL_PORT=$MYSQL_PORT
  fi

  read -rp "On which port should the adminer listen on ? [$ADMINER_PORT] : " newAdminer
    if [ "$newAdminer" != '' ]; then
      export ADMINER_PORT=$newAdminer;
    else
      export ADMINER_PORT=$ADMINER_PORT;
    fi

  runCommand;
}

promptForEnv(){
  read -rp "Automatically read environment variable ? [y/N] :" yn
    case $yn in
        [Yy]* ) {
            export $(cat ./environment/kotlin/.env.prod | grep -v '#' | awk '/=/ {print $1}')
            export $(cat ./environment/mysql/.env.prod | grep -v '#' | awk '/=/ {print $1}')
            runCommand;
        };;
        [Nn]* ) promptForPorts;;
        * ) echo "Please only answer yes [y] or no [n]."; loadEnvironment;;
    esac
}

loadEnvironment(){
  if [ -f ./environment/kotlin/.env.prod ] && [ -f ./environment/mysql/.env.prod ]; then
    promptForEnv
  else
    promptForPorts
  fi
}

promptWorkspace(){
  read -rp "Start development or production core ? [d/P] :" dp
      case $dp in
          [Dd]* ) {
              export ENVIRONMENT=dev
              loadEnvironment
          };;
          [Pp]* ) {
            export ENVIRONMENT=prod
            loadEnvironment
          };;
          * ) echo "Please only answer development [d] or production [p]."; promptWorkspace;;
      esac
}

runCommand(){
  sudo docker-compose -f docker-compose.yml up --build
}

promptWorkspace;
