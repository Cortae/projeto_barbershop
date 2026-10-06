import { BrowserRouter, Routes, Route } from "react-router-dom"
import { ProdutosPagina } from "../pages/produtos/ProdutosPagina"

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ProdutosPagina />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
