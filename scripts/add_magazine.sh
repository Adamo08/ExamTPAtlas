#!/bin/bash

# Base URL of the application
BASE_URL="http://localhost:9090/TPExamAtlas_war_exploded"

# Function to display usage
usage() {
    echo "Usage: $0 <title> <publisher> <issueNumber> <dateIssue>"
    echo "Example: $0 \"Scientific American\" \"Springer Nature\" \"Vol 328 No 4\" \"2025-04-01\""
    exit 1
}

# Check number of arguments
if [ "$#" -ne 4 ]; then
    echo "Error: Incorrect number of arguments."
    usage
fi

# Arguments
TITLE="$1"
PUBLISHER="$2"
ISSUE_NUMBER="$3"
DATE_ISSUE="$4"

# Basic validation
if [[ -z "$TITLE" || -z "$PUBLISHER" || -z "$ISSUE_NUMBER" || -z "$DATE_ISSUE" ]]; then
    echo "Error: All fields (title, publisher, issueNumber, dateIssue) must be provided."
    usage
fi
if ! echo "$DATE_ISSUE" | grep -qE "^[0-9]{4}-[0-9]{2}-[0-9]{2}$"; then
    echo "Error: dateIssue must be in YYYY-MM-DD format."
    usage
fi

# Send POST request and capture response/status
echo "Sending request to $BASE_URL/documents/"
RESPONSE=$(curl -X POST "$BASE_URL/documents/" \
     -d "type=magazine" \
     -d "title=$TITLE" \
     -d "publisher=$PUBLISHER" \
     -d "issueNumber=$ISSUE_NUMBER" \
     -d "dateIssue=$DATE_ISSUE" \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -w "\n%{http_code}" -s)

# Split response and status code
BODY=$(echo "$RESPONSE" | sed '$d')
STATUS=$(echo "$RESPONSE" | tail -n1)

echo "HTTP Status: $STATUS"
echo "Response: $BODY"

if [ "$STATUS" -ne 200 ]; then
    echo "Error: Failed to add magazine. Check server logs for details."
    exit 1
fi

echo ""