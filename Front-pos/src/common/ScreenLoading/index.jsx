import "./ScreenLoading.css";

function ScreenLoading(props) {
  return (
    <section className="screenLoadingContainer">
      <div id="loadingSpinner"></div>
      <h1 className="loadingText">Cargando...</h1>
    </section>
  );
}
export { ScreenLoading };
