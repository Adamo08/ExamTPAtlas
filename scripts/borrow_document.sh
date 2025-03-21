#!/bin/bash

# Base URL of the application
BASE_URL="http://localhost:9090/ExamTPAtlas_war_exploded"

# Function to display usage
usage() {
    echo "Usage: $0 <userId> <documentId>"
    echo "Example: $0 1 1"
    exit 1
}

# Check number of arguments
if [ "$#" -ne 2 ]; then
    echo "Error: Incorrect number of arguments."
    usage
fi

# Arguments
USER_ID="$1"
DOCUMENT_ID="$2"

# Basic validation
if [[ -z "$USER_ID" || -z "$DOCUMENT_ID" ]]; then
    echo "Error: userId and documentId cannot be empty."
    usage
fi
if ! echo "$USER_ID" | grep -qE "^[0-9]+$" || ! echo "$DOCUMENT_ID" | grep -qE "^[0-9]+$"; then
    echo "Error: userId and documentId must be numeric."
    usage
fi

# Send POST request and capture response/status
echo "Sending request to $BASE_URL/borrowings/"
RESPONSE=$(curl -X POST "$BASE_URL/borrowings/" \
     -d "userId=$USER_ID" \
     -d "documentId=$DOCUMENT_ID" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -w "\n%{http_code}" -s)

# Split response and status code
BODY=$(echo "$RESPONSE" | sed '$d')
STATUS=$(echo "$RESPONSE" | tail -n1)

echo "HTTP Status: $STATUS"
echo "Response: $BODY"

if [ "$STATUS" -ne 200 ]; then
    echo "Error: Failed to borrow document. Check server logs for details."
    exit 1
fi

echo ""