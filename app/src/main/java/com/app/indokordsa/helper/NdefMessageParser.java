package com.app.indokordsa.helper;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.app.indokordsa.interfaces.ParsedNdefRecord;
import com.app.indokordsa.helper.SmartPoster;
import com.app.indokordsa.helper.TextRecord;
import com.app.indokordsa.helper.UriRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {

    private NdefMessageParser() {
    }

    public static List<ParsedNdefRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<>();

        for (final NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record));
            } else {
                elements.add(() -> new String(record.getPayload()));
            }
        }

        return elements;
    }
}