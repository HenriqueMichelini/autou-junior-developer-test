import FormSection from "./form-section/FormSection";

function ContainerArea() {
  return (
    <div className="flex flex-row gap-2 w-full h-full rounded-sm">
      <FormSection />
      <FormSection />
    </div>
  );
}

export default ContainerArea;
