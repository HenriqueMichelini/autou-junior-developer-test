import Header from "./header/header";
import ContentArea from "./content-area/ContentArea";

function PageWrapper() {
  return (
    <div className="flex flex-col h-screen">
      <Header title="Email" subtitle="subtitle" />
      <ContentArea />
    </div>
  );
}

export default PageWrapper;
