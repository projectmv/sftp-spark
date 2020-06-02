package br.com.sftp.core


class FTPFunc {

    import java.io.{FileOutputStream, InputStream, BufferedOutputStream}
    import org.apache.commons.net.ftp.{FTPClient, FTPFile, FTP}
    import scala.util.Try
    private val client = new FTPClient

    def login(username: String, password: String): Try[Boolean] = Try {
        client.login(username, password)
        client.setFileType(FTP.BINARY_FILE_TYPE)
    }

    def connect(host: String): Try[Unit] = Try {
        client.connect(host)
    }

    def connected: Boolean = client.isConnected
    def disconnect(): Unit = client.disconnect()

    def listFiles(dir: Option[String] = None): List[FTPFile] =
        dir.fold(client.listFiles)(client.listFiles).toList

    def connectWithAuth(host: String,
                        username: String = "",
                        password: String = "") : Try[Boolean] = {
        for {
            connection <- connect(host)
            login      <- login(username, password)
        } yield login
    }

    def cd(path: String): Boolean =
        client.changeWorkingDirectory(path)

    def uploadFile(remote: String, input: InputStream): Boolean = {
        client.storeFile(remote, input)
    }

    def downloadFile(fileName: String): Boolean = {
        val outputStream1 = new BufferedOutputStream(new FileOutputStream(fileName))
        val success = client.retrieveFile(fileName, outputStream1)
        outputStream1.close()
//        if (success) System.out.println("File #1 has been downloaded successfully.")
        true
    }

    def makeDir(path: String): Boolean =
        client.makeDirectory(path)

    def getReply: String =
        client.getReplyString
}