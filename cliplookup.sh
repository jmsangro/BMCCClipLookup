#!/bin/bash
java --module-path /home/administrator/javafx-sdk-21.0.7/lib --add-modules javafx.controls,javafx.fxml,javafx.media -cp /home/administrator/BMCCClipLookup/lib/commons-lang3-3.17.0.jar:/home/administrator/BMCCClipLookup/lib/opencsv-5.8.jar:/home/administrator/BMCCClipLookup/bin -DdataFile="/home/administrator/BMCCClipLookup/Test Data.csv" cliplookup.ClipLookupMain
