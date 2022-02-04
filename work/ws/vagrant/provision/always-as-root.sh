#!/usr/bin/env bash

#
# Copyright © 2020-2022 EC2U Consortium. All rights reserved.
#

#== Bash helpers ==

function info {
  echo " "
  echo "--> $1"
  echo " "
}

#== Provision script ==

info "Provision-script user: `whoami`"

info "Restart web-stack"
service php7.2-fpm restart
service nginx restart
service mysql restart
