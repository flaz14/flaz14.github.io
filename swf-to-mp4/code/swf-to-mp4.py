#!/usr/bin/env python3

#from subprocess import call as sh


import subprocess
import re

output = subprocess.check_output(["ls", "-l"])
print("output is [{0}]".format(output))


# explore SWF-file
width_raw = subprocess.check_output(['swfdump', '--width', '../video/tricky.swf'])
print(width_raw)
width_pretty = str(width_raw, "utf-8") # TODO write a remark about used encoding



width_parsed = re.compile("-X (\d+)", re.S).search(width_pretty).groups()[0]
print(width_parsed)

width = int(width_parsed)
print(width)