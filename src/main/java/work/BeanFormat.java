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

import com.metreeca.rest.*;
import com.metreeca.rest.formats.*;

import java.io.*;
import java.text.ParseException;

import static com.metreeca.rest.MessageException.status;
import static com.metreeca.rest.Response.BadRequest;
import static com.metreeca.rest.Response.UnsupportedMediaType;
import static com.metreeca.rest.Toolbox.service;
import static com.metreeca.rest.formats.InputFormat.input;
import static com.metreeca.rest.formats.OutputFormat.output;

import static work.BeanCodec.codec;

public final class BeanFormat<T> extends Format<T> {

    public static <T> Format<T> bean(final Class<T> type) {

        if ( type == null ) {
            throw new NullPointerException("null type");
        }

        return new BeanFormat<>(type);

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Class<T> type;


    private BeanFormat(final Class<T> type) {
        this.type=type;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the default MIME type for bean messages ({@value JSONFormat#MIME})
     */
    @Override public String mime() {
        return JSONFormat.MIME;
    }


    /**
     * Decodes the bean {@code message} body from the input stream supplied by the {@code message} {@link InputFormat}
     * body, if one is available and the {@code message} {@code Content-Type} header is either missing or  matched by
     * {@link JSONFormat#MIMEPattern}
     */
    @Override public Either<MessageException, T> decode(final Message<?> message) {
        return message

                .header("Content-Type")

                .filter(JSONFormat.MIMEPattern.asPredicate().or(String::isEmpty))

                .map(mime -> message.body(input()).flatMap(source -> {

                    try (
                            final InputStream input=source.get();
                            final Reader reader=new InputStreamReader(input, message.charset())
                    ) {

                        return Either.Right(service(codec()).decode(reader, type));

                    } catch ( final UnsupportedEncodingException|ParseException e ) {

                        return Either.Left(status(BadRequest, e));

                    } catch ( final IOException e ) {

                        throw new UncheckedIOException(e);

                    }

                }))

                .orElseGet(() -> Either.Left(status(UnsupportedMediaType, "no JSON body")));
    }

    /**
     * Configures {@code message} {@code Content-Type} header to {@value JSONFormat#MIME}, unless already defined, and
     * encodes the bean {@code value} into the output stream accepted by the {@code message} {@link OutputFormat} body
     */
    @Override public <M extends Message<M>> M encode(final M message, final T value) {
        return message

                .header("~Content-Type", mime())

                .body(output(), output -> {
                    try ( final Writer writer=new OutputStreamWriter(output, message.charset()) ) {

                        service(codec()).encode(writer, value);

                    } catch ( final IOException e ) {

                        throw new UncheckedIOException(e);

                    }
                });
    }

}
