#!/usr/bin/env python3

# -*- coding: utf-8 -*-


import subprocess
import sys

#apt-get download <имя_пакета>=<версия> и apt-get source <имя_пакета>=<версия>


def packages(file_descriptor):
	raw_lines = file_descriptor.readlines()
	pretty_lines = filter(
		None, 
		[line.strip() for line in raw_lines]
	)
	packages = []
	for line in pretty_lines:
		name_and_version = line.split()
		name = name_and_version[0]
		version = name_and_version[1]
		packages.append(
			{
				'name' : name,
				'version' : version
			}
		)
	print(packages)


def dump(package):
	print(
		'{} {}'.format(package[name], package[version])
	)


def download(packages):
	for package in packages:
		dump(package)
		

def download_binary():
	pass


def download_source():
	pass


class AptGetCommand:
	@staticmethod
	def download(package_name):
		return [
			'apt-get',
			'download',
			'{}={}'.format(package_name, package_version)
		]

	@staticmethod
	def source(package_name, package_version):
		return [
			'apt-get',
			'source',
			'{}={}'.format(package_name, package_version)
		]


def main():
	packages(sys.stdin)


if __name__ == '__main__':
	main()	