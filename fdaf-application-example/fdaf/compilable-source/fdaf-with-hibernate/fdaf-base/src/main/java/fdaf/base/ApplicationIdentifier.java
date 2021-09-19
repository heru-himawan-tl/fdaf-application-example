/*
 * Copyright (c) Heru Himawan Tejo Laksono. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fdaf.base;

public abstract class ApplicationIdentifier extends FrameworkIdentifier {

    protected static final String USER_SESSION_ID_FIELD_NAME = "FDAF";

    protected ApplicationIdentifier() {
        // NO-OP
    }
    
    public String getApplicationCodeName() {
        return "fdaf";
    }
    
    public String getApplicationName() {
        return "FDAF";
    }
    
    public String getApplicationLongName() {
        return "FDAF Application Example";
    }
    
    public String getApplicationDescription() {
        return "An application example shows you how to build efficiently a sample of Java EE application based FDAF framework. This is a quick overview of the most common FDAF starters, along with examples on how to use the FDAF framework API's and abstractions.";
    }
    
    public String getApplicationDevelCopyright() {
        return "Copyright (C) Heru Himawan Tejo Laksono";
    }
    
    public String getApplicationDevelHomePage() {
        return "https://github.com/heru-himawan-tl/fdaf-application-example";
    }
    
    public String getApplicationVersion() {
        return "1.0";
    }
    
    public String getApplicationCompiledDate() {
        return "2021-09-20 at 05:40:22 WIB";
    }
}