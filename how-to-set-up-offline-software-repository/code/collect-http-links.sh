#!/usr/bin/env bash

declare -r release_name='trusty'
declare -r base_url='http://archive.ubuntu.com/ubuntu/dists'

lynx -dump -listonly -nonumbers "$base_url"  | grep "$release_name"
