/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.grida.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Rafael Silva
 */
public class SocketCommunication extends Communication {

    private BufferedReader in;
    private PrintWriter out;

    public SocketCommunication(Socket socket) throws IOException {

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    @Override
    public String getMessage() throws IOException {

        StringBuilder messageBuilder = new StringBuilder();

        String message = in.readLine();
        if (message == null || message.startsWith(Constants.MSG_ERROR)) {
            throw new IOException(message);
        }

        while (!message.equals(Constants.MSG_END)) {
            if (!messageBuilder.toString().isEmpty()) {
                messageBuilder.append(Constants.MSG_SEP_1);
            }
            messageBuilder.append(message);
            message = in.readLine();
        }

        return messageBuilder.toString();
    }

    @Override
    public void close() throws IOException {
        
        out.close();
    }
}
