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

package work;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.text.ParseException;
import java.time.*;
import java.util.function.Supplier;

public interface BeanCodec {

    public static Supplier<BeanCodec> codec() {
        return GsonCodec::new;
    }


    public default String encode(final Object bean) {

        if ( bean == null ) {
            throw new NullPointerException("null bean");
        }

        try ( final Writer writer=new StringWriter() ) {

            return encode(writer, bean).toString();

        } catch ( final IOException unexpected ) {

            throw new UncheckedIOException(unexpected);

        }
    }

    public default <T> T decode(final String json, final Class<T> type) throws ParseException {

        if ( json == null ) {
            throw new NullPointerException("null json");
        }

        if ( type == null ) {
            throw new NullPointerException("null type");
        }

        try ( final Reader reader=new StringReader(json) ) {

            return decode(reader, type);

        } catch ( final IOException unexpected ) {

            throw new UncheckedIOException(unexpected);

        }
    }


    public <W extends Writer> W encode(final W writer, final Object bean) throws IOException;

    public <T> T decode(final Reader reader, final Class<T> type) throws IOException, ParseException;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final class GsonCodec implements BeanCodec {

        private final Gson gson=new GsonBuilder()

                .setPrettyPrinting()

                .registerTypeAdapter(Instant.class, new InstantTimeTypeAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(Period.class, new PeriodTypeAdapter())

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


        private static final class InstantTimeTypeAdapter extends TypeAdapter<Instant> {

            @Override public void write(final JsonWriter writer, final Instant value) throws IOException {
                writer.value(value.toString());
            }

            @Override public Instant read(final JsonReader reader) throws IOException {
                return Instant.parse(reader.nextString());
            }

        }

        private static final class ZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

            @Override public void write(final JsonWriter writer, final ZonedDateTime value) throws IOException {
                writer.value(value.toString());
            }

            @Override public ZonedDateTime read(final JsonReader reader) throws IOException {
                return ZonedDateTime.parse(reader.nextString());
            }

        }

        private static final class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

            @Override public void write(final JsonWriter writer, final LocalDate value) throws IOException {
                writer.value(value.toString());
            }

            @Override public LocalDate read(final JsonReader reader) throws IOException {
                return LocalDate.parse(reader.nextString());
            }

        }

        private static final class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {

            @Override public void write(final JsonWriter writer, final LocalTime value) throws IOException {
                writer.value(value.toString());
            }

            @Override public LocalTime read(final JsonReader reader) throws IOException {
                return LocalTime.parse(reader.nextString());
            }

        }

        private static final class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

            @Override public void write(final JsonWriter writer, final LocalDateTime value) throws IOException {
                writer.value(value.toString());
            }

            @Override public LocalDateTime read(final JsonReader reader) throws IOException {
                return LocalDateTime.parse(reader.nextString());
            }

        }

        private static final class YearTypeAdapter extends TypeAdapter<Year> {

            @Override public void write(final JsonWriter writer, final Year value) throws IOException {
                writer.value(value.toString());
            }

            @Override public Year read(final JsonReader reader) throws IOException {
                return Year.parse(reader.nextString());
            }

        }

        private static final class DurationTypeAdapter extends TypeAdapter<Duration> {

            @Override public void write(final JsonWriter writer, final Duration value) throws IOException {
                writer.value(value.toString());
            }

            @Override public Duration read(final JsonReader reader) throws IOException {
                return Duration.parse(reader.nextString());
            }

        }

        private static final class PeriodTypeAdapter extends TypeAdapter<Period> {

            @Override public void write(final JsonWriter writer, final Period value) throws IOException {
                writer.value(value.toString());
            }

            @Override public Period read(final JsonReader reader) throws IOException {
                return Period.parse(reader.nextString());
            }

        }

    }

}
