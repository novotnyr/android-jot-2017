package sk.upjs.ics.jot.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public interface JotContract {
    String AUTHORITY = "sk.upjs.ics.jot";

    interface Note extends BaseColumns {
        String TABLE_NAME = "note";

        Uri CONTENT_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

        String DESCRIPTION = "description";

        String TIMESTAMP = "timestamp";
    }
}