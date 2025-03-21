#!/bin/bash

# Base URL of the application
BASE_URL="http://localhost:9090/ExamTPAtlas_war_exploded/"

# Function to display usage
usage() {
    echo "Usage: $0 <title> <author> <isbn> <datePubl>"
    echo "Example: $0 \"To Kill a Mockingbird\" \"Harper Lee\" \"978-0446310789\" \"1960-07-11\""
    exit 1
}

# Check number of arguments
if [ "$#" -ne 4 ]; then
    echo "Error: Incorrect number of arguments."
    usage
fi

# Arguments
TITLE="$1"
AUTHOR="$2"
ISBN="$3"
DATE_PUBL="$4"

# Basic validation
if [[ -z "$TITLE" || -z "$AUTHOR" || -z "$ISBN" || -z "$DATE_PUBL" ]]; then
    echo "Error: All fields (title, author, isbn, datePubl) must be provided."
    usage
fi
if ! echo "$DATE_PUBL" | grep -qE "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"; then
    echo "Error: datePubl must be in YYYY-MM-DD format."
    usage
fi

# Send POST request and capture response/status
echo "Sending request to $BASE_URL/documents/"
RESPONSE=$(curl -X POST "$BASE_URL/documents/" \
     -d "type=book" \
     -d "title=$TITLE" \
     -d "author=$AUTHOR" \
     -d "isbn=$ISBN" \
     -d "datePubl=$DATE_PUBL" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -w "\n%{http_code}" -s)

# Split response and status code
BODY=$(echo "$RESPONSE" | sed '$d')
STATUS=$(echo "$RESPONSE" | tail -n1)

echo "HTTP Status: $STATUS"
echo "Response: $BODY"

if [ "$STATUS" -ne 200 ]; then
    echo "Error: Failed to add book. Check server logs for details."
    exit 1
fi

echo ""