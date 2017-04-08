#!/usr/bin/env python3

import subprocess
import re

# explore SWF-file
raw_info = subprocess.check_output(['swfdump', '--width', '--height', '--rate', '../video/tricky.swf'])
print(raw_info)
pretty_info = str(raw_info, "utf-8") # TODO write a remark about used encoding
parsed_info = re.compile("-X (?P<width>\d+) -Y (?P<height>\d+) -r (?P<rate>\d+\.\d+)", re.S).search(pretty_info).groupdict()
print(parsed_info)

