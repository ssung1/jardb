#!/bin/bash

cygwin=false

case "`uname`" in
CYGWIN*) cygwin=true;
esac

# this part gets the program name
prog_name="$0"

# -h means symbolic link, in which case we get the link target
while [ -h $prog_name ]; do
  ls=`ls -ld $prog_name`
  link=`expr $ls : '.*-> \(.*\)$'`
  if expr $link : '.*/.*' > /dev/null; then
    prog_name=$link
  else
    prog_name=`dirname $prog_name`/$link
  fi
done

# get the path to this shell script so we can set the classpath
prog_dir=`dirname $prog_name`

#java -Xms64M -Xmx256M -Xrunhprof:cpu=samples com.covista.traffic.Resource "$@"
"$prog_dir/runjava.sh" \
    name.subroutine.jardb.util.Search \
    "$@"

