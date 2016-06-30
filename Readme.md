Have maven installed on the machine with appropriate M2_HOME settings in env variables.
Execute mvn package to generate project jar.
Modify go.sh to point to the generated jar.
Run "chmod 755 go.sh" with su access and execute go.sh from terminal.