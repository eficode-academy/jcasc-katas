#!/bin/bash

rm keys/{id*,authorized_keys}
cd keys
ssh-keygen -f id_rsa -t rsa -P ""
cp id_rsa.pub authorized_keys
cd ..
