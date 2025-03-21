#!/bin/bash

# Base URL of the application
BASE_URL="http://localhost:9090/TPExamAtlas_war_exploded"

# Function to display usage
usage() {
    echo "Usage: $0 <name> <mail>"
    echo "Example: $0 \"Alice Johnson\" \"alice.johnson@example.com\""
    exit 1
}

# Check number of arguments
if [ "$#" -ne 2 ]; then
    echo "Error: Incorrect number of arguments."
    usage
fi

# Arguments
NAME="$1"
MAIL="$2"

# Basic validation
if [[ -z "$NAME" || -z "$MAIL" ]]; then
    echo "Error: Name and mail cannot be empty."
    usage
fi
if ! echo "$MAIL" | grep -qE "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"; then
    echo "Error: Invalid email format."
    usage
fi

# Send POST request and capture response/status
echo "Sending request to $BASE_URL/users/"
RESPONSE=$(curl -X POST "$BASE_URL/users/" \
     -d "name=$NAME" \
     -d "mail=$MAIL" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -w "\n%{http_code}" -s)

# Split response and status code
BODY=$(echo "$RESPONSE" | sed '$d')
STATUS=$(echo "$RESPONSE" | tail -n1)

echo "HTTP Status: $STATUS"
echo "Response: $BODY"

if [ "$STATUS" -ne 200 ]; then
    echo "Error: Failed to add user. Check server logs for details."
    exit 1
fi

echo ""