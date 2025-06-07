#!/bin/bash

# ----------------------------------------
# Script: extract-country-cities.sh
# Description: Extract unique cities and their counts for a given country from an IP-location CSV.
# Options:
#   -c COUNTRY     Country code (default: CN)
#   -f FILE        Input CSV file (default: geoloc-Microsoft.csv)
#   -s true|false  Whether to skip header line (default: true)
#   -o FILE        Output CSV file path (optional). If not provided, output only to terminal.
# ----------------------------------------

# Default values
COUNTRY_CODE="CN"
INPUT_FILE="geoloc-Microsoft.csv"
SKIP_HEADER="true"
OUTPUT_FILE=""  # not set by default

# Parse command-line options
while getopts "c:f:s:o:" opt; do
  case ${opt} in
    c ) COUNTRY_CODE="$OPTARG" ;;
    f ) INPUT_FILE="$OPTARG" ;;
    s )
      if [[ "$OPTARG" != "true" && "$OPTARG" != "false" ]]; then
        echo "❌ Error: -s must be 'true' or 'false'"
        exit 1
      fi
      SKIP_HEADER="$OPTARG"
      ;;
    o )
      OUTPUT_FILE="$OPTARG"
      ;;
    \? )
      echo "Usage: $0 [-c country_code] [-f input_file.csv] [-s true|false] [-o output_file.csv]"
      exit 1
      ;;
  esac
done

# Check if file exists
if [[ ! -f "$INPUT_FILE" ]]; then
  echo "❌ Error: File '$INPUT_FILE' not found."
  exit 1
fi

# Log input parameters
echo "📄 Input File     : $INPUT_FILE"
echo "🌐 Country Code   : $COUNTRY_CODE"
echo "⏩ Skip Header    : $SKIP_HEADER"
if [[ -n "$OUTPUT_FILE" ]]; then
  echo "📤 Output File    : $OUTPUT_FILE"
else
  echo "📤 Output File    : (none, print to terminal only)"
fi

# Run awk
awk -F',' -v code="$COUNTRY_CODE" -v skip_header="$SKIP_HEADER" -v out_file="$OUTPUT_FILE" '
  BEGIN {
    line_num = 0
  }
  {
    line_num++
    if (skip_header == "true" && line_num == 1)
      next
    if ($2 == code && $4 != "") {
      count[$4]++
    }
  }
  END {
    if (out_file != "") {
      print "City,Count" > out_file
      for (city in count) {
        printf "%s,%d\n", city, count[city] >> out_file
      }
      close(out_file)
      print "✅ Output written to " out_file
    }
    for (city in count) {
      printf "%-20s %d\n", city, count[city]
    }
  }
' "$INPUT_FILE"

echo "✅ Done."
