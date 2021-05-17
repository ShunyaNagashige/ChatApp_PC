package jp.ac.meijo_u.id190441091.pc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ChildThreadTask implements Runnable {
	// ソケット変数の宣言
	private Socket socket;

	private static String message = null;

	public ChildThreadTask(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		DataInputStream socketDIS = null;
		DataOutputStream socketDOS = null;
		String buf = null;

		try {
			// ソケットの入出力ストリームを取得し，データ入力ストリームを連結
			socketDIS = new DataInputStream(socket.getInputStream());
			socketDOS = new DataOutputStream(socket.getOutputStream());

			while (true) {
				try {
					if (message != buf) {
						socketDOS.writeUTF(message);
						socketDOS.flush();
						buf = message;
					}

					message = socketDIS.readUTF();
				} catch (EOFException e) {
					message = null;
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socketDIS != null)
				try {
					socketDIS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (socketDOS != null)
				try {
					socketDOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
