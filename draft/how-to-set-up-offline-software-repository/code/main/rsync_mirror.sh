#!/usr/bin/env bash

rsync --archive \
      --progress \
      --verbose \
      --bwlimit=128K \
      --files-from=uris.txt \
      --dry-run \
      -vv \
      'rsync://archive.ubuntu.com/ubuntu/pool/***' repo