package jp.ac.meijo_u.id190441091.pc;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ChatServer {
	/** �ő�ڑ���(10�N���C�A���g) */
	private static final int MAX_CONNECTION = 10;
	/** �T�[�o�\�P�b�g */
	private static ServerSocket serverSocket;
	/** �q�X���b�h�̔z��(message�Ńf�[�^����肷��񂾂���C�����v�邩�H) */
	private static ArrayList<Thread> childThreadList;

	/**
	 * main���\�b�h
	 * 
	 * @param args [0]���b�X���|�[�g�ԍ�
	 */
	public static void main(String[] args) {
		// �R�}���h���C���̈������`�F�b�N
		if (args.length != 1) {
			System.out.println("�������K�؂ł͂���܂���B");
			System.out.println("usage:ChatServer port");
			System.exit(1);
		}

		// �R�}���h���C���������烊�b�X������|�[�g�ԍ���ݒ�
		int port = Integer.parseInt(args[0]);

		// �\�P�b�g�ϐ��̐錾
		Socket socket = null;
//		DataInputStream socketDIS = null;
//		DataOutputStream socketDOS = null;

		ChildThreadTask task=null;
		Thread thread=null;
		
		try {
			// �T�[�o�\�P�b�g�̐���
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			// �w��|�[�g�ԍ��Ƀo�C���h
			serverSocket.bind(new InetSocketAddress(port), MAX_CONNECTION);
			System.out.println("�|�[�g" + port + "�ł����b�X����...");

			while (true) {
				/*
				 * �N���C�A���g����̐ڑ��v����ҋ@�i�N���C�A���g�ڑ�����܂Ńu���b�L���O�j �[�ڑ�����������ƁC�N���C�A���g�ƒʐM���邽�߂̃\�P�b�g�����������
				 */
				socket = serverSocket.accept();
				// �ڑ������N���C�A���g�̏���\��
				showClientInformation(socket);
				
				task=new ChildThreadTask(socket);
				thread=new Thread(task);
				thread.start();
				
				//�K�v�Ȃ̂��H
				childThreadList.add(thread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			// �f�[�^����M�p�X�g���[�������
//			if (socketDIS != null)
//				try {
//					socketDIS.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			if (socketDOS != null)
//				try {
//					socketDOS.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static void showClientInformation(Socket socket) throws IOException {
		// �N���C�A���g��IP�A�h���X���擾
		InetAddress address = socket.getInetAddress();
		// �N���C�A���g�̃|�[�g�ԍ����擾
		int port = socket.getPort();

		System.out.println("�N���C�A���g[" + address.toString() + ":" + port + "]���ڑ����܂����B");
	}
}
