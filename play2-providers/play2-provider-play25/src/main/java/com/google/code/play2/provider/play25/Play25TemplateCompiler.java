/*
 * Copyright 2013-2015 Grzegorz Slowikowski (gslowikowski at gmail dot com)
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

package com.google.code.play2.provider.play25;

import java.io.File;
import java.util.Collections;
import java.util.List;

import scala.Option;
import scala.io.Codec;

import play.TemplateImports;

import play.twirl.compiler.TemplateCompilationError;
import play.twirl.compiler.TwirlCompiler;

import com.google.code.play2.provider.api.Play2TemplateCompiler;
import com.google.code.play2.provider.api.TemplateCompilationException;

public class Play25TemplateCompiler
    implements Play2TemplateCompiler
{
    private static final String[] templateExts = {
        "html",
        "txt",
        "xml",
        "js" };

    private static final String[] formatterTypes = {
        "play.twirl.api.HtmlFormat",
        "play.twirl.api.TxtFormat",
        "play.twirl.api.XmlFormat",
        "play.twirl.api.JavaScriptFormat" };

    private String mainLang;

    private File sourceDirectory;

    private File outputDirectory;

    @Override
    public String getCustomOutputDirectoryName()
    {
        return "twirl";
    }

    @Override
    public void setMainLang( String mainLang )
    {
        this.mainLang = mainLang;
    }

    @Override
    public void setSourceDirectory( File sourceDirectory )
    {
        this.sourceDirectory = sourceDirectory;
    }

    @Override
    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public File compile( File templateFile )
        throws TemplateCompilationException
    {
        File result = null;

        String fileName = templateFile.getName();
        String ext = fileName.substring( fileName.lastIndexOf( '.' ) + 1 );
        String importsAsString = getImportsAsString( ext );
        int index = getTemplateExtIndex( ext );
        if ( index >= 0 )
        {
            String formatterType = formatterTypes[index];
            try
            {
                Option<File> resultOption =
                    TwirlCompiler.compile( templateFile, sourceDirectory, outputDirectory, formatterType,
                                           importsAsString, Codec.apply( "UTF-8" )/* codec */, false/* inclusiveDot */, false/* useOldParser */ );
                result = resultOption.isDefined() ? resultOption.get() : null;
            }
            catch ( TemplateCompilationError e )
            {
                throw new TemplateCompilationException( e.source(), e.message(), e.line(), e.column() );
            }
        }
        return result;
    }

    private int getTemplateExtIndex( String ext )
    {
        int result = -1;
        for ( int i = 0; i < templateExts.length; i++ )
        {
            if ( templateExts[i].equals( ext ) )
            {
                result = i;
                break;
            }
        }
        return result;
    }

    private String getImportsAsString( String format )
    {
        List<String> additionalImports = Collections.emptyList();
        if ( "java".equalsIgnoreCase( mainLang ) )
        {
            additionalImports = TemplateImports.defaultJavaTemplateImports;
        }
        else if ( "scala".equalsIgnoreCase( mainLang ) )
        {
            additionalImports = TemplateImports.defaultScalaTemplateImports;
        }
        StringBuilder sb = new StringBuilder();
        for ( String additionalImport : additionalImports )
        {
            sb.append( "import " ).append( additionalImport.replace( "%format%", format ) ).append( "\n" );
        }
        return sb.substring( 0, sb.length() - 1 );
    }

}