package juego.control;

import java.util.ArrayList;
import java.util.List;

import juego.modelo.*;
import juego.util.CoordenadasIncorrectasException;
import juego.util.Sentido;

/**
 * Modelo abstracto de arbitro.
 * 
 * Contiene las funciones comunes a ArbitroSimple y ArbitroEstandar.
 * 
 * @author <a href="mailto:btx1002@alu.ubu.es">Beatrice Izabel Toth</a>
 * @author <a href="mailto:lma1006@alu.ubu.es">Luis Martín Asenjo</a>
 * 
 * @since Java 16
 * @version 4.0
 * @see juego.control.ArbitroSimple
 * @see juego.control.ArbitroEstandar
 */
public abstract class ArbitroAbstracto implements Arbitro{
	
	/** Array de torres negras a colocar en el tablero. */
	protected List<TorreAbstracta> torresN;
	/** Array de torres blancas a colocar en el tablero. */
	protected List<TorreAbstracta> torresB;
	
	/** Número de jugada actual. */
	protected static int jugada;
	/** Número de ronda actual. */
	protected static int ronda;
	/** Turno actual. */
	protected static Turno turno = null;
	/** Turno contrario al actual. */
	private Turno turno2;
	/** Tablero de Kamisado. */
	protected Tablero tablero = new Tablero();
	/** Puntos del turno blanco. */
	protected static int puntosBlanco;
	/** Pûntos del turno negro. */
	protected static int puntosNegro;

	/**
	 * Constructor de arbitro abstracto.
	 * 
	 * Inicialmente se inicializan los arrays de torres y el tablero.
	 * Después se inicia la jugada a 0, la ronda a 1 y los puntos a 0.
	 * 
	 * @param tablero tablero de kamisado
	 */
	public ArbitroAbstracto(Tablero tablero) {
		this.tablero = tablero;
		this.torresN = new ArrayList<TorreAbstracta>();
		this.torresB = new ArrayList<TorreAbstracta>();
		
		torresN = generarTorres(false);
		torresB = generarTorres(true);
		
		jugada = 0;
		ronda = 1;
		puntosBlanco = 0;
		puntosNegro = 0;
		turno = null;
	}
	
	/**
	 * Retorna la celda que contiene la torre del turno y color indicado.
	 * 
	 * @param turno turno
	 * @param color color
	 * @return celda
	 */
	public Celda buscarCeldaConTorreDeColor(Turno turno, Color color) {
		return tablero.buscarTorre(turno, color);
	}

	
	/**
	 * Genera arrays torres de turno blanco y negro con el orden de colores correcto.
	 * 
	 * <p>
	 * Posteriormente, esos arrays deberían usarse para colocar las torres en el tablero
	 * dentro de un bucle.
	 * </p>
	 * 
	 * @param tipo true para turno blanco, false para turno negro
	 * @return array de torres del turno indicado
	 */
	public List<TorreAbstracta> generarTorres(boolean tipo) {
		List<TorreAbstracta> torres;
		torres = new ArrayList<TorreAbstracta>();
		
		if (tipo == false) {
			torres.add(new TorreSimple(Turno.NEGRO, Color.MARRON));
			torres.add(new TorreSimple(Turno.NEGRO, Color.VERDE));
			torres.add(new TorreSimple(Turno.NEGRO, Color.ROJO));
			torres.add(new TorreSimple(Turno.NEGRO, Color.AMARILLO));
			torres.add(new TorreSimple(Turno.NEGRO, Color.ROSA));
			torres.add(new TorreSimple(Turno.NEGRO, Color.PURPURA));
			torres.add(new TorreSimple(Turno.NEGRO, Color.AZUL));
			torres.add(new TorreSimple(Turno.NEGRO, Color.NARANJA));
		} else {
			torres.add(new TorreSimple(Turno.BLANCO, Color.NARANJA));
			torres.add(new TorreSimple(Turno.BLANCO, Color.AZUL));
			torres.add(new TorreSimple(Turno.BLANCO, Color.PURPURA));
			torres.add(new TorreSimple(Turno.BLANCO, Color.ROSA));
			torres.add(new TorreSimple(Turno.BLANCO, Color.AMARILLO));
			torres.add(new TorreSimple(Turno.BLANCO, Color.ROJO));
			torres.add(new TorreSimple(Turno.BLANCO, Color.VERDE));
			torres.add(new TorreSimple(Turno.BLANCO, Color.MARRON));
		}
		
		return torres;
	}
	
	/**
	 * Coloca las torres de ambos turnos en su posición inicial e indica el turno inicial. 
	 */
	public void colocarTorres() {		
		for (int i = 0; i < tablero.obtenerNumeroColumnas(); i++) {
			try {
				tablero.colocar(torresN.get(i), tablero.obtenerCelda(7, i));
				tablero.colocar(torresB.get(i), tablero.obtenerCelda(0, i));
			} catch (CoordenadasIncorrectasException ex) {
				throw new RuntimeException();
			}
		}
		
		if (ronda == 1) {
			turno = Turno.NEGRO;
		}
		
		tablero.ultimoColorTurnoBlanco = null;
		tablero.ultimoColorTurnoNegro = null;
	}
	
	/**
	 * Coloca las torres de ambos turnos en la posición indicada e indica el turno actual.
	 * 
	 * @param torres array de torres a colocar en el tablero
	 * @param coordenadas coordenadas donde se colocan las torres en el tablero
	 * @param ultimoColorTurnoNegro último color del turno negro
	 * @param ultimoColorTurnoBlanco último color del turno blanco
	 * @param turnoActual turno actual
	 */
	public void colocarTorres(Torre[] torres, String[] coordenadas, Color ultimoColorTurnoNegro,
			Color ultimoColorTurnoBlanco, Turno turnoActual)  throws CoordenadasIncorrectasException {
		
		turno = turnoActual;
		tablero.ultimoColorTurnoBlanco = ultimoColorTurnoBlanco;
		tablero.ultimoColorTurnoNegro = ultimoColorTurnoNegro;
		
		if(turno == null) {
			turno = Turno.NEGRO;
		}
		
		for(int i = 0; i < torres.length; i++) {
			tablero.colocar(torres[i], coordenadas[i]);
		}
	}
	
	/**
	 * Comprueba si el movimiento es legal para el turno actual.
	 * 
	 * @param origen celda origen
	 * @param destino celda destino
	 * @return true si el movimiento es legal o false en caso contrario
	 * @throws CoordenadasIncorrectasException si las coordenadas de alguna de las celdas son incorrectas
	 */
	public boolean esMovimientoLegalConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {		
		if (obtenerTurno() == Turno.NEGRO) {
			turno2 = Turno.BLANCO;
		} else {
			turno2 = Turno.NEGRO;
		}
		try {
			if (turno == Turno.NEGRO
					&& (tablero.obtenerSentido(origen, destino) == Sentido.DIAGONAL_NE
						|| tablero.obtenerSentido(origen, destino) == Sentido.VERTICAL_N 
						|| tablero.obtenerSentido(origen, destino) == Sentido.DIAGONAL_NO)
					&& (origen.obtenerColorDeTorre() == obtenerUltimoMovimiento(turno2) || jugada < 2)
					&& (destino.estaVacia() 
							&& !origen.estaVacia())
					&& tablero.estanVaciasCeldasEntre(origen, destino)
					&& (origen.obtenerTorre().obtenerMaximoAlcance() >= tablero.obtenerDistancia(origen, destino)
							|| origen.obtenerTorre().obtenerMaximoAlcance() == 0)) {
				return true;
				
			} else if (turno == Turno.BLANCO
					&& (tablero.obtenerSentido(origen, destino) == Sentido.DIAGONAL_SE 
						|| tablero.obtenerSentido(origen, destino) == Sentido.VERTICAL_S
						|| tablero.obtenerSentido(origen, destino) == Sentido.DIAGONAL_SO)
					&& (origen.obtenerColorDeTorre() == obtenerUltimoMovimiento(turno2) || jugada < 2)
					&& (destino.estaVacia() 
							&& !origen.estaVacia())
					&& tablero.estanVaciasCeldasEntre(origen, destino)
					&& (origen.obtenerTorre().obtenerMaximoAlcance() >= tablero.obtenerDistancia(origen, destino)
						|| origen.obtenerTorre().obtenerMaximoAlcance() == 0)) {
				
				return true;

			} else
				return false;
		} catch (CoordenadasIncorrectasException ex) {
			
			throw new CoordenadasIncorrectasException("Las coordenadas de las celdas son incorrectas.");
		}
	}
	
	/**
	 * Cambia el último color del turno actual.
	 */
	public void cambiarUltimoColor() {
		if (turno == Turno.NEGRO) {
			tablero.ultimoColorTurnoNegro = tablero.ultimoColorTurnoBlanco;
		} else if (turno == Turno.BLANCO){
			tablero.ultimoColorTurnoBlanco = tablero.ultimoColorTurnoNegro;
		}
	}
	
	/**
	 * Comprueba si está bloqueado el turno actual.
	 * 
	 * @return true si no puede realizar ningún movimiento, false en caso contrario
	 */
	public boolean estaBloqueadoTurnoActual() {
		Boolean flag;
		Celda origen;
			if (jugada < 2) {
				flag = false;
			} else {
				
				if (obtenerTurno() == Turno.NEGRO) {
					origen = tablero.buscarTorre(obtenerTurno(), obtenerUltimoMovimiento(Turno.BLANCO));
					if (origen.obtenerFila() == 0) {
						flag = false;
					} else if (origen.obtenerColumna() == 0 || origen.obtenerColumna() == 7) {
						flag = comprobarBloqueoLaterales(origen, turno);
					} else {
						flag = comprobarBloqueo(origen, turno);
					}
					
				} else {
					origen = tablero.buscarTorre(obtenerTurno(), obtenerUltimoMovimiento(Turno.NEGRO));
					if (origen.obtenerFila() == 7) {
						flag = false;
					} else if (origen.obtenerColumna() == 0 || origen.obtenerColumna() == 7) {
						flag = comprobarBloqueoLaterales(origen, turno);
					} else {
						flag = comprobarBloqueo(origen, turno);
					}
				}
			}
		if (flag == true) {
			//moverConTurnoActualBloqueado();
		}		
		return flag;
	}
	
	/**
	 * Comprueba si la torre ubicada en la celda indicada está bloqueada.
	 * 
	 * Método para uso general.
	 * 
	 * @param origen celda donde se encuentra la torre
	 * @param turno turno de la torre
	 * @return true si está bloqueada o false en caso contrario
	 */
	public boolean comprobarBloqueo(Celda origen, Turno turno) {
		Celda destino1, destino2, destino3;
		try {
			if (obtenerTurno() == Turno.NEGRO) {
				destino1 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna());
				destino2 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna()-1);
				destino3 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna()+1);
			} else {
				destino1 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna());
				destino2 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna()-1);
				destino3 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna()+1);
			}
			if (!esMovimientoLegalConTurnoActual(origen, destino1) && !esMovimientoLegalConTurnoActual(origen, destino2)
					&& !esMovimientoLegalConTurnoActual(origen, destino3)) {
				return true;
			}
		} catch (CoordenadasIncorrectasException ex) {
			throw new RuntimeException();
		}
		return false;
	}
	
	/**
	 * Comprueba si la torre ubicada en la celda indicada está bloqueada.
	 * 
	 * Método para torres en los bordel del tablero.
	 * 
	 * @param origen celda donde se encuentra la torre
	 * @param turno turno de la torre
	 * @return true si está bloqueada o false en caso contrario
	 */
	public boolean comprobarBloqueoLaterales(Celda origen, Turno turno) {
		Celda destino1, destino2;
		try {
			if (obtenerTurno() == Turno.NEGRO) {
				if (origen.obtenerColumna() == 0) {
					destino1 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna());
					destino2 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna()+1);
				} else {
					destino1 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna());
					destino2 = tablero.obtenerCelda(origen.obtenerFila()-1, origen.obtenerColumna()-1);
				}
			} else {
				if (origen.obtenerColumna() == 0) {
					destino1 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna());
					destino2 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna()+1);
				} else {
					destino1 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna());
					destino2 = tablero.obtenerCelda(origen.obtenerFila()+1, origen.obtenerColumna()-1);
				}
			}
			if (!esMovimientoLegalConTurnoActual(origen, destino1) && !esMovimientoLegalConTurnoActual(origen, destino2)) {
				return true;
			}
		} catch (CoordenadasIncorrectasException ex) {
			throw new RuntimeException();
		}
		return false;
	}

	/**
	 * Cambia de turno en funcion del turno actual.
	 */
	protected void cambiarTurno() {	
		
		  if (turno == Turno.NEGRO) {
			turno = Turno.BLANCO;
		} else if (turno == Turno.BLANCO) {
			turno = Turno.NEGRO;
		}
	}
	
	/**
	 * Comprueba si hay situación de bloqueo mutuo o deadlock.
	 * 
	 * @return true si hay bloqueo mutuo o deadlock, false en caso contrario
	 */
	public boolean hayBloqueoMutuo() {
		
		boolean bloqA = estaBloqueadoTurnoActual();
		cambiarTurno();
		boolean bloqB = estaBloqueadoTurnoActual();
		cambiarTurno();		
		
		if(bloqA == true && bloqB == true) {
			return true;
		}
		return false;
	}

	/**
	 * Mueve la torre de la celda origen a la celda destino.
	 * 
	 * @param origen celda origen
	 * @param destino celda destino
	 * @throws CoordenadasIncorrectasException si las coordenadas de alguna de las celdas son incorrectas
	 */
	public void moverConTurnoActual(Celda origen, Celda destino) throws CoordenadasIncorrectasException {
		/*if (estaBloqueadoTurnoActual()) {
			moverConTurnoActualBloqueado();
		}*/
		
		try {
			if(esMovimientoLegalConTurnoActual(origen, destino)) {
				tablero.moverTorre(origen, destino);
				cambiarTurno();
			}
			
			jugada++;
			obtenerUltimoMovimiento(turno);
		}catch(CoordenadasIncorrectasException ex) {
			throw new RuntimeException();
		}
		
	
	}

	/**
	 * Obtiene el turno actual.
	 * 
	 * @return color del turno actual
	 */
	public Turno obtenerTurno() {
		return turno;
	}

	/**
	 * Devuelve el número de jugadas realizadas hasta el momento.
	 * 
	 * @return número actual de jugada
	 */
	public int obtenerNumeroJugada() {
		return jugada;
	}

	/**
	 * Mueve la torre del turno actual a su propia posición por estar bloqueada.
	 */
	public void moverConTurnoActualBloqueado() {
		jugada++;
		cambiarTurno();
	}

	/**
	 * Consulta el color de celda del último movimiento del turno indicado.
	 * 
	 * @param turno turno
	 * @return color de celda del último movimiento del turno indicado
	 */
	public Color obtenerUltimoMovimiento(Turno turno) {
		
			if (turno.toChar() == 'N' && tablero.ultimoColorTurnoNegro != null) {
				return tablero.ultimoColorTurnoNegro;
			} else if (turno.toChar() == 'B' && tablero.ultimoColorTurnoBlanco != null) {
				return tablero.ultimoColorTurnoBlanco;
			}
		
		return null;
	}

	/**
	 * Consulta la puntuación del jugador con turno blanco.
	 * 
	 * @return puntuación del jugador con turno blanco
	 */
	public int obtenerPuntuacionTurnoBlanco() {
		return puntosBlanco;
	}

	/**
	 * Consulta la puntuación del jugador con turno negro.
	 * 
	 * @return puntuación del jugador con turno negro
	 */
	public int obtenerPuntuacionTurnoNegro() {
		return puntosNegro;
	}

	/**
	 * Elimina todas las torres del tablero.
	 */
	public void vaciarTablero() {
		for (int i = 0; i < tablero.obtenerNumeroFilas(); i++) {
			for (int j = 0; j < tablero.obtenerNumeroColumnas(); j++) {
				try {
					if (!tablero.obtenerCelda(i, j).estaVacia()) {
						tablero.obtenerCelda(i, j).eliminarTorre();
					}
				} catch (CoordenadasIncorrectasException ex) {
					throw new RuntimeException();
				}
			}
		}
	}
	
	/**
	 * Comprueba si el turno negro ha llegado al final del tablero.
	 * 
	 * @return true si ha llegado al final o false en caso contrario
	 */
	public boolean NegroAlcanzaFinal() {
			if(tablero.hayTorreColorContrario(Turno.NEGRO)) {
				return true;
			}
		return false;
	}
	
	/**
	 * Comprueba si el turno blanco ha llegado al final del tablero.
	 * 
	 * @return true si ha llegado al final o false en caso contrario
	 */
	public boolean BlancoAlcanzaFinal() {
			if(tablero.hayTorreColorContrario(Turno.BLANCO)) {
				return true;
			}
		return false;
	}
	
	/**
	 * Comprueba si el turno indicado ha llegado al final del tablero.
	 * 
	 * @param turno turno
	 * 
	 * @return true en caso afirmativo o false en caso contrario
	 */
	@Override
	public boolean estaAlcanzadaUltimaFilaPor(Turno turno) {
		if (NegroAlcanzaFinal() && turno == Turno.NEGRO) {
			return true;
			
		} else if(BlancoAlcanzaFinal() && turno == Turno.BLANCO) {
			return true;	
		}
		return false;
	}
}