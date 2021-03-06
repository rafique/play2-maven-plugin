/*
 * Copyright 2013-2018 Grzegorz Slowikowski (gslowikowski at gmail dot com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.google.code.play2.provider.api;

import java.io.File;

public class Play2BuildException
    extends Exception
{
    private static final long serialVersionUID = 1L;

    private File source;

    private int line;

    private int position;

    public Play2BuildException( File source, String message, int line, int position )
    {
        super( message );
        this.source = source;
        this.line = line;
        this.position = position;
    }

    public Play2BuildException( Throwable cause, File source, String message, int line, int position )
    {
        super( message, cause );
        this.source = source;
        this.line = line;
        this.position = position;
    }

    /**
     * Error line number, if defined.
     * 
     * @return exception line number
     */
    public int line()
    {
        return line;
    }

    /**
     * Column position, if defined.
     * 
     * @return exception column position
     */
    public int position()
    {
        return position;
    }

    /**
     * Source file.
     * 
     * @return exception source file
     */
    public File source()
    {
        return source;
    }

}
