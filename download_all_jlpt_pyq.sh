#!/bin/bash
# Script to download all official JLPT Previous Year Question papers (N1 to N5)

TARGET_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/jlpt_pyq_papers"
mkdir -p "$TARGET_DIR"
cd "$TARGET_DIR"

echo "Downloading official JLPT Question Papers (N1 - N5)..."

URLS=(
  "https://www.jlpt.jp/e/samples/pdf/2018_N1.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N1_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N1.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N1_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N2.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N2_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N2.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N2_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N3.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N3_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N3.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N3_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N4.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N4_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N4.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N4_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N5.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2018_N5_ans.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N5.pdf"
  "https://www.jlpt.jp/e/samples/pdf/2012_N5_ans.pdf"
)

for url in "${URLS[@]}"; do
  filename=$(basename "$url")
  echo "Downloading $filename..."
  curl -s -O "$url"
done

echo "Done! All JLPT PYQ PDFs downloaded to: $TARGET_DIR"
