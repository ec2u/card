/*
 * Copyright © 2020-2022 EC2U Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.ec2u.card.services;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import static java.time.temporal.ChronoField.*;

public final class Codec {

    private static final DateTimeFormatter DateFormat=new DateTimeFormatterBuilder()

            .parseStrict()

            .appendValue(YEAR, 4)

            .optionalStart()
            .appendLiteral('-')
            .optionalEnd()

            .appendValue(MONTH_OF_YEAR, 2)

            .optionalStart()
            .appendLiteral('-')
            .optionalEnd()

            .appendValue(DAY_OF_MONTH, 2)

            .toFormatter(Locale.ROOT);

    private static final DateTimeFormatter TimeFormat=new DateTimeFormatterBuilder()

            .parseStrict()

            .appendValue(HOUR_OF_DAY, 2)

            .optionalStart()
            .appendLiteral(':')
            .optionalEnd()

            .appendValue(MINUTE_OF_HOUR, 2)

            .optionalStart()
            .appendLiteral(':')
            .optionalEnd()

            .appendValue(SECOND_OF_MINUTE, 2)

            .optionalStart()
            .appendLiteral('.')
            .appendValue(MILLI_OF_SECOND)
            .optionalEnd()

            .toFormatter(Locale.ROOT);

    private static final DateTimeFormatter DateTimeFormat=new DateTimeFormatterBuilder()

            .parseStrict()

            .append(DateFormat)

            .appendLiteral("T")

            .append(TimeFormat)

            .optionalStart()
            .appendLiteral('Z')
            .optionalEnd()

            .toFormatter(Locale.ROOT);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Gson gson=new GsonBuilder()

            .setPrettyPrinting()

            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())

            .create();


    public <W extends Writer> W encode(final W writer, final Object bean) throws IOException {

        if ( writer == null ) {
            throw new NullPointerException("null writer");
        }

        if ( bean == null ) {
            throw new NullPointerException("null bean");
        }

        try {

            gson.toJson(bean, writer);

            return writer;

        } catch ( final JsonIOException e ) {

            throw new IOException(e);

        }
    }

    public <T> T decode(final Reader reader, final Class<T> type) throws IOException, ParseException {

        if ( reader == null ) {
            throw new NullPointerException("null reader");
        }

        if ( type == null ) {
            throw new NullPointerException("null type");
        }

        try {

            return gson.fromJson(reader, type);

        } catch ( final JsonSyntaxException e ) {

            throw new ParseException(e.getMessage(), 0);

        } catch ( final JsonIOException e ) {

            throw new IOException(e);

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

        @Override public void write(final JsonWriter writer, final LocalDate value) throws IOException {
            writer.value(value.toString());
        }

        @Override public LocalDate read(final JsonReader reader) throws IOException {
            return LocalDate.parse(reader.nextString(), DateFormat);
        }

    }

    public static final class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

        @Override public void write(final JsonWriter writer, final LocalDateTime value) throws IOException {
            writer.value(value.toString());
        }

        @Override public LocalDateTime read(final JsonReader reader) throws IOException {
            return LocalDateTime.parse(reader.nextString(), DateTimeFormat);
        }

    }

}