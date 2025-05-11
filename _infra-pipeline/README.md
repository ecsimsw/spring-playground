## Ports
- 8850 : config
- 8851 : account
- 8852 : auth
- 8853 : event
- 8854 : notification
- 8855 : transaction

## Build and push
- ./build-and-deploy.sh api-config 1.0.0
- ./build-and-deploy.sh api-account 1.0.1
- ./build-and-deploy.sh api-auth 1.0.0
- ./build-and-deploy.sh api-notification 1.0.1

## Pull and run
- ./pull-and-run.sh api-config 1.0.0 8850
- ./pull-and-run.sh api-notification 1.0.0 8854