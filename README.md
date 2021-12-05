# This program create compile_commands.json for your smart text editor 

You need write all your compile commands in defines.clj command variable (like include paths, and other)

## Installation

### whit jvm clojure
Download jdk, clojure from clojure site and leiningen
change command variable and project dir variable
write 
$ lein run 
and then you get compile_commands.json file 
(you need using full path for directories, then you set includes)
Parsing time is 2-3 minute for very large project, but it ones
### whit babashka
bb core.clj
## Usage

FIXME: explanation

    $ java -jar omar-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
# compile-commands-creator
