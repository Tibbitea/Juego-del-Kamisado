package juego.control;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;

/**
 * Arbitro estandar.
 * 
 * Contiene las funciones específicas para partidas estandar.
 * 
 * @author <a href="mailto:btx1002@alu.ubu.es">Beatrice Izabel Toth</a>
 * @author <a href="mailto:lma1006@alu.ubu.es">Luis Martín Asenjo</a>
 * 
 * @since Java 16
 * @version 4.0
 * @see juego.control.ArbitroAbstracto
 */
public class ArbitroEstandar extends ArbitroAbstracto {
	
	/**
	 * Constructor de arbitro estandar.
	 * 
	 * Inicialmente se inicializan los arrays de torres y el tablero.
	 * Después se inicia la jugada a 0, la ronda a 1 y los puntos a 0.
	 * 
	 * @param tablero tablero de kamisado
	 */
	public ArbitroEstandar(Tablero tablero) {
		super(tablero);
		
		jugada = 0;
		ronda = 1;
		turno = null;
	}
	
	/**
	 * Reinicia la ronda.
	 * 
	 * <p>
	 * Avanza a la ronda siguiente, pone el número de jugadas a 0 y
	 * coloca las torres tras vaciar el tablero.
	 * </p>
	 */
	@Override
	public void reiniciarRonda() {
		ronda++;
		jugada = 0;
		vaciarTablero();
		colocarTorres();
	}
	
	/**
	 * Convierte la torre ganadora de la ronda anterior en una torre sumo uno.
	 * 
	 * Posteriormente la introduce en el array de torres del turno al que pertenezca.
	 */
	public void crearTorreSumoUno() {
		if (tablero.torreSU != null) {

			if(tablero.torreSU.obtenerTurno() == Turno.NEGRO) {
				if (obtenerNumeroTorresSumoUno(Turno.NEGRO) <= 3) {
					for(int i = 0; i < super.torresN.size(); i++) {
						if (super.torresN.get(i).obtenerColor() == tablero.torreSU.obtenerColor()) {
							super.torresN.set(i, new TorreSumoUno(tablero.torreSU.obtenerTurno(), tablero.torreSU.obtenerColor()));
							tablero.torreSU = null;
							break;
						}
					}
				}
			} else {
				if (obtenerNumeroTorresSumoUno(Turno.BLANCO) <= 3) {
					for(int i = 0; i < super.torresB.size(); i++) {
						if (super.torresN.get(i).obtenerColor() == tablero.torreSU.obtenerColor()) {
							super.torresN.set(i, new TorreSumoUno(tablero.torreSU.obtenerTurno(), tablero.torreSU.obtenerColor()));
							tablero.torreSU = null;
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Devuelve el número de torres sumo uno del turno determinado.
	 * 
	 * @param turno turno de las torres sumo uno a buscar
	 * @return número de torres sumo uno del turno determinado
	 */
	public int obtenerNumeroTorresSumoUno(Turno turno) {
		int contadorTorres = 0;
		if (turno == Turno.NEGRO) {
			for(int i = 0; i < torresN.size(); i++) {
					if(torresN.get(i).obtenerNumeroDientes() == 1) {
						contadorTorres++;
					}
			}
		} else {
			for(int i = 0; i < torresB.size(); i++) {
					if(torresB.get(i).obtenerNumeroDientes() == 1) {
					contadorTorres++;
					}
			}
		}
		return contadorTorres;
	}
	
	/**
	 * Comprueba si la ronda actual está acabada.
	 * 
	 * @return true en caso afirmativo o false en caso contrario
	 */
	@Override
	public boolean estaAcabadaRonda() {
		if (consultarGanadorRonda() != null) {
			crearTorreSumoUno();
			reiniciarRonda();
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Consulta el ganador de la ronda.
	 * 
	 * <p>
	 * Un turno puede ganar llegando al final del tablero o por
	 * bloqueo mutuo.
	 * </p>
	 * 
	 * @return turno ganador de la ronda
	 */
	@Override
	public Turno consultarGanadorRonda() {
		Turno turno = obtenerTurno();
			if (jugada >= 2) {
				if (BlancoAlcanzaFinal()){
					puntosBlanco += tablero.buscarTorre(Turno.BLANCO, obtenerUltimoMovimiento(Turno.NEGRO)).obtenerTorre().obtenerNumeroPuntos();
					turno = Turno.NEGRO;
					return Turno.BLANCO;
				} else if (NegroAlcanzaFinal()){
					puntosNegro += tablero.buscarTorre(Turno.NEGRO, obtenerUltimoMovimiento(Turno.BLANCO)).obtenerTorre().obtenerNumeroPuntos();
					turno = Turno.BLANCO;
					return Turno.NEGRO;
				} else if (hayBloqueoMutuo()){
					
					if (turno == Turno.NEGRO) {
						puntosNegro += tablero.buscarTorre(Turno.NEGRO, obtenerUltimoMovimiento(Turno.NEGRO)).obtenerTorre().obtenerNumeroPuntos();
					} else {
						puntosBlanco += tablero.buscarTorre(Turno.BLANCO, obtenerUltimoMovimiento(Turno.BLANCO)).obtenerTorre().obtenerNumeroPuntos();
					}
					return turno;
				}
			}
			turno = Turno.NEGRO;
			return null;
		}
	
	/**
	 * Comprueba si la partida ha finalizado.
	 * 
	 * @return true si ha finalizado o false en caso contrario
	 */
	@Override
	public boolean estaAcabadaPartida() {
		if (consultarGanadorPartida() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Consulta el ganador de la partida.
	 * 
	 * Un turno puede ganar alcanzando o superando los 3 puntos.
	 * 
	 * @return turno ganador de la partida
	 */
	@Override
	public Turno consultarGanadorPartida() {
		if (obtenerPuntuacionTurnoBlanco() >= 3) {
			return Turno.BLANCO;
		} else if (obtenerPuntuacionTurnoNegro() >= 3) {
			return Turno.NEGRO;
		} else {
			return null;
		}
	}
	
	/**
	 * Realiza un empujón sumo.
	 * 
	 * @param origen celda origen
	 * @throws CoordenadasIncorrectasException si la celda origen o celdas del empujón están fuera del tablero
	 */
	@Override
	public void empujarSumo(Celda origen) throws CoordenadasIncorrectasException {
        if (esEmpujonSumoLegal(origen)) {
            if (origen.obtenerTurnoDeTorre() == Turno.NEGRO) {
            	int fila = origen.obtenerFila() - 1;
                int columna = origen.obtenerColumna();

                tablero.moverTorre(tablero.obtenerCelda(fila, columna), tablero.obtenerCelda(fila -1, columna));
                tablero.moverTorre(origen, tablero.obtenerCelda(fila, columna));
            } else {
            	int fila = origen.obtenerFila() + 1;
                int columna = origen.obtenerColumna();

                tablero.moverTorre(tablero.obtenerCelda(fila, columna), tablero.obtenerCelda(fila +1, columna));
                tablero.moverTorre(origen, tablero.obtenerCelda(fila, columna));
            }
            
            cambiarTurno();
            obtenerUltimoMovimiento(turno);
            cambiarTurno();
            jugada++;
        }
    }

	/**
	 * Comprueba si el empujón sumo es legal.
	 * 
	 * <p>
	 * Para que sea legal, tiene que realizarlo una torreSumoUno a
	 * una torreSimple del turno contrario. No se puede empujar una
	 * torre fuera del tablero.
	 * </p>
	 * 
	 * @param origen celda origen
	 * @return true si es legal o false en caso contrario
	 * @throws CoordenadasIncorrectasException si la celda origen o celdas del empujón están fuera del tablero
	 */
	@Override
	public boolean esEmpujonSumoLegal(Celda origen) throws CoordenadasIncorrectasException {
		Celda destino, celdaPosterior;
		Turno turnoContrario;
		if(ronda < 2) {
			return false;
		}
		
		if (origen.obtenerFila() < 2 || origen.obtenerFila() > 5) {
			return false;
		}
		
		if (turno == Turno.NEGRO) {
			destino = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna());
			celdaPosterior = tablero.obtenerCelda(destino.obtenerFila()-1, destino.obtenerColumna());
			turnoContrario = Turno.BLANCO;
		} else {
			destino = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna());
			celdaPosterior = tablero.obtenerCelda(destino.obtenerFila()+1, destino.obtenerColumna());
			turnoContrario = Turno.NEGRO;
		}
		
		if (!destino.estaVacia() && destino.obtenerTurnoDeTorre() == turnoContrario && destino.obtenerTorre().obtenerNumeroDientes() == 0) {
			if (celdaPosterior.estaVacia()) {
				return true;
			}
		}
		
		return false;
	}
}