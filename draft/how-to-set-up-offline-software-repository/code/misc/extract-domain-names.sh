#!/usr/bin/env bash

declare -r domain_name_regex='.*http://([a-z.]*) +'
declare -a domain_names

while read line; do
	[[ $line =~ $domain_name_regex ]] || continue
	domain_name="${BASH_REMATCH[1]}"
	# trim leading/trailing whitespaces and squeeze internal whitespaces
	pretty_name="$(echo "$domain_name" | xargs)" 
	domain_names+=("$pretty_name")
done < apt-get_update_sample.log

printf '%s\n' "${domain_names[@]}" | sort | uniq
